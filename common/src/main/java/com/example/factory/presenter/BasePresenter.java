package com.example.factory.presenter;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 12:46 2019/10/19
 */
public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {

    private T mView;
    public BasePresenter(T view){
        setmView(view);
    }

    protected void setmView(T view){
        this.mView = view;
        this.mView.setPresenter(this);
    }

    protected final T getmView(){
        return mView;
    }

    @Override
    public void start() {
        T view = mView;
        if (view != null){
            view.showLoading();
        }
    }

    @Override
    public void destory() {
        T view = mView;
        mView = null;
        if (view != null){
            view.setPresenter(null);
        }
    }
}
