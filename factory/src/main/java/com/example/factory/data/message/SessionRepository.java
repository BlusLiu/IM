package com.example.factory.data.message;

import androidx.annotation.NonNull;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 17:30 2020/4/4
 */
public class SessionRepository extends BaseDbRepository<Session> implements   SessionDataSoure {
    @Override
    protected boolean isRequired(Session session) {
        return true;
    }

    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Session.class)
                .orderBy(Session_Table.modifyAt, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }

    @Override
    protected void insert(Session session) {
        dataList.addFirst(session);
    }
}
