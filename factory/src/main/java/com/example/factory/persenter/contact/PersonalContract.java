package com.example.factory.persenter.contact;

import com.example.factory.model.db.User;
import com.example.factory.presenter.BaseContract;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 20:02 2019/10/26
 */
public interface PersonalContract {

    interface Presneter extends BaseContract.Presenter{
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presneter>{
        String getUserId();

        void onLoadDone(User user);
        void allowSayHellow(boolean isAllow);

        void setFollowStatus(boolean isFollow);
    }
}
