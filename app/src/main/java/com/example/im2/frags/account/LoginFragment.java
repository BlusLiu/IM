package com.example.im2.frags.account;


import android.content.Context;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.common.APP.Fragment;
import com.example.common.APP.PresenterFragment;
import com.example.factory.persenter.account.LoginContract;
import com.example.factory.persenter.account.LoginPersenter;
import com.example.im2.R;
import com.example.im2.activites.MainActivity;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends PresenterFragment<LoginContract.Presenter>
implements LoginContract.View{

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;

    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    private AccountTrigger accountTrigger;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPersenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 拿到我们的activity的应用
        accountTrigger = (AccountTrigger) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();


        mPresenter.login(phone, password);
    }

    @OnClick(R.id.text_go_login)
    void onRegisterClick(){
        accountTrigger.triggerView();
    }

    @Override
    public void showError(int Str) {
        super.showError(Str);
        // 当提示需要显示错误的时候

        // 停止Loading
        mLoading.stop();
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);

    }

    @Override
    public void showLoading() {
        super.showLoading();

        mLoading.start();
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }
}
