package com.example.factory.persenter.message;

import androidx.recyclerview.widget.DiffUtil;

import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.db.Message;
import com.example.factory.persenter.BaseSourcePresenter;
import com.example.factory.persistence.Account;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 9:47 2019/11/2
 */
public class ChatPresenter<View extends ChatContract.View>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.Presenter{

    protected String receiverId;
    protected int receiverType;

    public ChatPresenter(MessageDataSource source, View view, String receiverId, int receiverType) {
        super(source, view);
        this.receiverId = receiverId;
        this.receiverType = receiverType;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.View view = getmView();
        if (view == null)
            return;

        List<Message> old = view.getRecyclerAdapter().getItems();

        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);

        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        refreshData(result, messages);
    }

    @Override
    public void pushText(String content) {
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(receiverId, receiverType)
                .content(content, Message.TYPE_STR)
                .build();

        MessageHelper.push(model);
    }

    @Override
    public void pushAudio(String path) {

    }

    @Override
    public void pushImages(String[] paths) {

    }

    @Override
    public boolean rePush(Message message) {
        if(Account.isLogin() && Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED){
            message.setStatus(Message.STATUS_CREATED);

            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return  true;

        }
        return false;
    }
}
