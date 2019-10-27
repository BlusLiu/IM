package com.example.factory.data.user;

import com.example.factory.model.card.UserCard;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 10:50 2019/10/27
 */
public interface UserCenter {

    void dispatch(UserCard... cards);
}
