package com.example.factory.persenter.user;

import android.text.TextUtils;
import android.util.Log;

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
                    Log.e("PHOTO", "run: :" + photoFilePath + "//" + url);
                    if (TextUtils.isEmpty(url)){
//                        view.showError(R.string.data_upload_error);
                        Run.onUiAsync(new Action() {
                            @Override
                            public void call() {
                                view.showError(R.string.data_upload_error);
                            }
                        });

//                        UserUpdateModel model = new UserUpdateModel("", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1586068248190&di=ca211dcf174951d12d3044261111f323&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_70%2Cc_zoom%2Cw_640%2Fimages%2F20181229%2F4071d3af818b41fdb90c5a618dd5c190.jpeg", desc, isMan? 1: 2);
//                        //上传默认头像
//                        UserHelper.update(model, UpdateInfoPresenter.this);
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
