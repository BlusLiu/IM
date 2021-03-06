package com.example.factory.persenter.account;

import androidx.annotation.StringRes;

import com.example.factory.presenter.BaseContract;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 10:21 2019/10/19
 */
public interface LoginContract{
    interface View  extends BaseContract.View<LoginContract.Presenter>{
        // 注册成功
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
         void login(String phone, String password);
    }
}
