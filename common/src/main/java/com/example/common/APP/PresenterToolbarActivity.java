package com.example.common.APP;




import android.content.Context;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.common.R;
import com.example.factory.presenter.BaseContract;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 20:38 2019/10/25
 */
public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter> extends ToolbarActivity
implements BaseContract.View<Presenter>{
    protected Presenter mPresenter;

    protected abstract Presenter initPresenter();

    @Override
    protected void initBefore() {
        super.initBefore();

        initPresenter();
    }

    @Override
    public void showError(int Str) {
        if (mPlaceHolderView != null){
            mPlaceHolderView.triggerError(Str);
        }else
            Applocation.showToast(Str);
    }

    @Override
    public void showLoading() {
        // TODO 显示一个loading
        if (mPlaceHolderView != null){
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    protected void hideLoading(){
        if (mPlaceHolderView != null){
            mPlaceHolderView.triggerOk();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destory();
    }
}
