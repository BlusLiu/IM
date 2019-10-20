package com.example.factory.persenter.account;

import android.text.TextUtils;

import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * @Author: liuzhen
 * @Description:  登陆的逻辑实现
 * @Date: Create in 19:23 2019/10/19
 */
public class LoginPersenter extends BasePresenter<LoginContract.View>
implements LoginContract.Presenter, DataSource.Callback<User> {
    public LoginPersenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();

        final LoginContract.View view = getmView();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)){
            view.showError(R.string.data_account_login_invalid_parameter);
        }else {
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, this);
        }
    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContract.View view = getmView();
        if (view == null){
            return;
        }
        // 强制执行主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataNotLoaded(final int num) {
        final LoginContract.View view = getmView();
        if (view == null){
            return;
        }
        // 强制执行主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
                view.showError(num);
            }
        });
    }
}
