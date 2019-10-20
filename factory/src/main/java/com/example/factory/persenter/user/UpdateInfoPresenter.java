package com.example.factory.persenter.user;

import android.text.TextUtils;

import com.example.common.APP.Applocation;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.net.UploadHelper;
import com.example.factory.persenter.account.LoginContract;
import com.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 18:45 2019/10/20
 */
public class UpdateInfoPresenter
        extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {
    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void update(final String photoFilePath, final String desc, final boolean isMan) {
        start();

        final UpdateInfoContract.View view = getmView();

        if (TextUtils.isEmpty(photoFilePath)||TextUtils.isEmpty(desc)){
            view.showError(R.string.data_account_update_invalid_parameter);
        }else {
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadPortrait(photoFilePath);
                    if (TextUtils.isEmpty(url)){
                        view.showError(R.string.data_upload_error);
                    }else {
                        UserUpdateModel model = new UserUpdateModel("", url, desc, isMan? 1: 2);
                        //上传
                        UserHelper.update(model, UpdateInfoPresenter.this);
                    }
                }
            });
        }
    }

    @Override
    public void onDataLoaded(UserCard userCard) {
        final UpdateInfoContract.View view = getmView();
        if (view == null){
            return;
        }
        // 强制执行主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里进行回调的时候一定就是主线程状态了
                view.updateSucceed();
            }
        });
    }

    @Override
    public void onDataNotLoaded(final int num) {
        final UpdateInfoContract.View view = getmView();
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
