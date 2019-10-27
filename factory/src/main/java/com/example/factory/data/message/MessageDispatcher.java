package com.example.factory.data.message;

import android.text.TextUtils;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 11:15 2019/10/27
 */
public class MessageDispatcher implements MessageCenter {
    private static MessageCenter instance;
    // 单线程池
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static MessageCenter instance(){
        if (instance == null){
            // 同步线程
            synchronized (MessageDispatcher.class){
                if (instance == null)
                    instance = new MessageDispatcher();
            }
        }
        return instance;
    }

    @Override
    public void dispatch(MessageCard... cards) {
        if (cards == null || cards.length == 0)
            return;
        executor.execute(new MessageCardHandler(cards));
    }

    private class MessageCardHandler implements Runnable{
        final private MessageCard[] cards;

        private MessageCardHandler(MessageCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Message> messages = new ArrayList<>();
            // 遍历
            for (MessageCard card : cards) {
                // 卡片基础信息过滤，错误卡片直接过滤
                if (card == null || TextUtils.isEmpty(card.getSenderId())
                        || TextUtils.isEmpty(card.getId())
                        || (TextUtils.isEmpty(card.getReceiverId())
                        && TextUtils.isEmpty(card.getGroupId())))
                    continue;

                Message message = MessageHelper.findFromLocal(card.getId());

                // 本地已经显示则不做处理
                if (message != null){
                    if (message.getStatus() == Message.STATUS_DONE)
                        continue;
                    if (card.getStatus() == Message.STATUS_DONE)
                        // 代表发送成功
                        message.setCreateAt(card.getCreateAt());

                    // TODO 更新一些变化的内容
                }else {
                    User sender = UserHelper.search(card.getSenderId());
                    User receiver = null;
                    Group group = null;

                    if (!TextUtils.isEmpty(card.getReceiverId())){
                        receiver = UserHelper.search(card.getReceiverId());
                    }else if (!TextUtils.isEmpty(card.getGroupId())){
                        group = GroupHelper.findFromLocal(card.getGroupId());
                    }

                    if (receiver == null || group == null)
                        continue;

                    message = card.build(sender, receiver, group);
                }
                messages.add(message);
            }
            if (messages.size() > 0)
                DbHelper.save(Message.class, messages.toArray(new Message[0]));
        }
    }
}
