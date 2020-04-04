package com.example.im2.frags.account;


import android.content.Context;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.example.common.APP.Applocation;
import com.example.common.APP.Fragment;
import com.example.common.APP.PresenterFragment;
import com.example.factory.persenter.account.RegisterContract;
import com.example.factory.persenter.account.RegisterPresenter;
import com.example.im2.R;
import com.example.im2.activites.MainActivity;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View {
    private AccountTrigger accountTrigger;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_password)
    EditText mPassword;

    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;



    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 拿到我们的activity的应用
        accountTrigger = (AccountTrigger) context;
    }


    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone = mPhone.getText().toString();
        String name  = mName.getText().toString();
        String password = mPassword.getText().toString();


        mPresenter.register(phone, name, password);
    }

    @OnClick(R.id.text_go_login)
    void onLoginClick(){
        accountTrigger.triggerView();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }


    @Override
    public void registerSuccess() {
        Applocation.showToast("注册成功");
        //super.showError("注册成功");
        mSubmit.setEnabled(false);
        mPhone.setEnabled(false);
        mName.setEnabled(false);
        mPassword.setEnabled(false);
        // 注册成功，这个时候账户已经登陆
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    public void showError(int Str) {
        super.showError(Str);
        // 当提示需要显示错误的时候

        // 停止Loading
        mLoading.stop();
        mPhone.setEnabled(true);
        mName.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);

    }

    @Override
    public void showLoading() {
        super.showLoading();

        mLoading.start();
        mPhone.setEnabled(false);
        mName.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }
}
