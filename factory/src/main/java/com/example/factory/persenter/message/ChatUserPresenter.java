package com.example.factory.persenter.message;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.data.message.MessageRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 9:45 2019/11/2
 */
public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
implements ChatContract.Presenter{


    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        // 数据源， View， 接收者， 接收者的类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();
        User mReceiver = UserHelper.searchFirstOfLoc(receiverId);
        getmView().onInit(mReceiver);
    }
}
