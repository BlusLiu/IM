package com.example.factory.presenter;

import androidx.annotation.StringRes;

/**
 * @Author: liuzhen
 * @Description: 公共的基本契约
 * @Date: Create in 10:33 2019/10/19
 */
public interface BaseContract {

    // 继承T
    interface View<T extends Presenter> {

        void showError(@StringRes int Str);

        void showLoading();

        void setPresenter(T presenter);
    }

    interface Presenter{

        void start();

        void destory();
    }
}
