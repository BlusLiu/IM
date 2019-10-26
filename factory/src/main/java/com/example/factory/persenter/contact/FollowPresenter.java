package com.example.factory.persenter.contact;

import com.example.factory.data.DataSource;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 11:18 2019/10/26
 */
public class FollowPresenter extends BasePresenter<FollowContact.View> implements FollowContact.Presenter, DataSource.Callback<UserCard> {
    public FollowPresenter(FollowContact.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();

        UserHelper.follow(id, this);
    }

    @Override
    public void onDataLoaded(final UserCard userCard) {
        final FollowContact.View view = getmView();

        if (view != null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(userCard);
                }
            });
        }
    }

    @Override
    public void onDataNotLoaded(final int num) {
        final FollowContact.View view = getmView();

        if (view != null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(num);
                }
            });
        }
    }
}
