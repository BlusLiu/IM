package com.example.factory.persenter.contact;

import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.presenter.BaseContract;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 11:16 2019/10/26
 */
public interface FollowContact {

    interface Presenter extends BaseContract.Presenter{
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter>{
        void onFollowSucceed(UserCard userCard);
    }
}
