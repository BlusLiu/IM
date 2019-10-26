package com.example.factory.persenter.contact;

import com.example.factory.Factory;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.db.User;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BasePresenter;
import com.google.gson.internal.bind.util.ISO8601Utils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 20:40 2019/10/26
 */
public class PersonalPresenter extends BasePresenter<PersonalContract.View> implements PersonalContract.Presneter {
    private String id;
    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);

    }

    @Override
    public void start() {
        super.start();

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getmView();
                if (view != null){
                    String id = view.getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    view.onLoadDone(user);
                    onLoaded(view, user);
                }
            }
        });
    }

    private void onLoaded(final PersonalContract.View view , final User user){
        this.user = user;

        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        final boolean isFollow = isSelf || user.isFollow();
        final boolean allowSayHello = isFollow && !isSelf;

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHellow(allowSayHello);
            }
        });
    }

    @Override
    public User getUserPersonal() {
        return null;
    }
}
