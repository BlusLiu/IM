package com.example.factory.persenter.user;

import com.example.factory.presenter.BaseContract;
import com.example.factory.presenter.BasePresenter;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 17:08 2019/10/20
 */
public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter {
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter>{
        // 回调成功
        void updateSucceed();
    }
}
