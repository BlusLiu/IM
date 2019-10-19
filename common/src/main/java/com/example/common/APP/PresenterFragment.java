package com.example.common.APP;

import android.content.Context;

import com.example.factory.presenter.BaseContract;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 10:46 2019/10/19
 */
public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    protected abstract Presenter initPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 界面初始化之后就触发
        initPresenter();
    }

    @Override
    public void showError(int Str) {
        Applocation.showToast(Str);
    }

    @Override
    public void showLoading() {
        // TODO 显示一个loading
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
