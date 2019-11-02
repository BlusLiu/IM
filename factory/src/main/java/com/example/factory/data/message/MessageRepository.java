package com.example.factory.data.message;

import androidx.annotation.NonNull;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.example.utils.CollectionUtil;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 9:52 2019/11/2
 */
public class MessageRepository extends BaseDbRepository<Message> implements MessageDataSource{
    // 对象的Id
    private String receiverId;

    public MessageRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    protected boolean isRequired(Message message) {
        return (receiverId.equalsIgnoreCase(message.getSender().getId() ) && message.getGroup() == null)
                || (message.getReceiver()!=null && receiverId.equalsIgnoreCase(message.getReceiver().getId()));

    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                    .and(Message_Table.sender_id.eq(receiverId))
                    .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
