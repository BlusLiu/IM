package com.example.factory.persenter.account;

import com.example.factory.presenter.BasePresenter;

/**
 * @Author: liuzhen
 * @Description:  登陆的逻辑实现
 * @Date: Create in 19:23 2019/10/19
 */
public class LoginPersenter extends BasePresenter<LoginContract.View>
implements LoginContract.Presenter{
    public LoginPersenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }
}
