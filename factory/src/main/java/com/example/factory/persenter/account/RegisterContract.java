package com.example.factory.persenter.account;

import androidx.annotation.StringRes;

import com.example.factory.presenter.BaseContract;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 10:21 2019/10/19
 */
public interface RegisterContract  {
    interface View extends BaseContract.View<RegisterContract.Presenter>{
        // 注册成功
        void registerSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
         void register(String phone, String name, String password);

         boolean checkMobile(String phone);
    }
}
