package com.example.factory.model.api.message;

import android.os.Build;

import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.persistence.Account;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 11:06 2019/11/2
 */
public class MsgCreateModel {

    private String id; // Id

    private String content;// 内容

    private String attach;// 附件，附属信息

    private int type = Message.TYPE_STR;// 消息类型

    private int receiverType = Message.RECEIVER_TYPE_NONE;// 如果是群信息，对应群Id

    private String receiverId;// 接收者Id

    public MsgCreateModel() {
        this.id = UUID.randomUUID().toString();
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getAttach() {
        return attach;
    }

    public int getType() {
        return type;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public String getReceiverId() {
        return receiverId;
    }


    public static class Builder{
        private MsgCreateModel model;

        public Builder() {
            this.model = new MsgCreateModel();
        }

        public Builder receiver(String receiverId, int receiverType){
            model.receiverId = receiverId;
            model.receiverType = receiverType;
            return this;
        }

        public Builder content(String content, int type){
            this.model.content = content;
            this.model.type  = type;
            return this;
        }

        public Builder attach(String attach){
            this.model.attach = attach;
            return this;
        }

        public MsgCreateModel build(){
            return this.model;
        }
    }

    private MessageCard card;
    public MessageCard buildCard(){
        if (card == null){
            MessageCard card = new MessageCard();
            card.setId(id);
            card.setContent(content);
            card.setAttach(attach);
            card.setType(type);
            card.setSenderId(Account.getUserId());

            if (receiverType == Message.RECEIVER_TYPE_GROUP){
                card.setGroupId(receiverId);
            }else {
                card.setReceiverId(receiverId);
            }

            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
            this.card = card;
        }
        return this.card;
    }
}
