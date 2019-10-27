package com.example.factory.data.message;

import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 11:13 2019/10/27
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
