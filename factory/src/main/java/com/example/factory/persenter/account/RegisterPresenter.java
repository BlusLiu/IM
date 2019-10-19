package com.example.factory.persenter.account;

import android.text.TextUtils;

import com.example.common.Common;
import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.User;
import com.example.factory.presenter.BasePresenter;


import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 12:38 2019/10/19
 */
public class RegisterPresenter
        extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User>{
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        // 调用开始
        start();

        RegisterContract.View view = getmView();



        // 校验
        if (!checkMobile(phone)){
            // 提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        }else if (name.length() < 2){
            view.showError(R.string.data_account_register_invalid_parameter_name);
        }else if (password.length() < 6){
            view.showError(R.string.data_account_register_invalid_parameter_password);
        }else {
            // TODO 请求
            RegisterModel model = new RegisterModel(phone, password, name);
            AccountHelper.register(model, this);
        }

    }

    /**
     * 检查手机号是否合法
     * @param phone
     * @return
     */
    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constance.REGEX_MOBILE,phone);
    }

    @Override
    public void onDataLoaded(User user) {
        // 请求成功，告知界面
        final RegisterContract.View view = getmView();
        if (view == null){
            return;
        }
        // 强制执行主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
               view.registerSuccess();
            }
        });
    }


    @Override
    public void onDataNotLoaded(final int num) {
        final RegisterContract.View view = getmView();
        if (view == null){
            return;
        }
        // 强制执行主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
                // 显示错误
                view.showError(num);
            }
        });
    }
}
