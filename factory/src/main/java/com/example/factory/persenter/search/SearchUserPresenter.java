package com.example.factory.persenter.search;

import com.example.factory.data.DataSource;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 21:42 2019/10/25
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView> implements SearchContract.Presenter, DataSource.Callback<List<UserCard>>{
    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();

        // 这里可以利用call优化一下
        UserHelper.search(content, this);
    }

    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        final SearchContract.UserView view = getmView();
        if (view != null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(userCards);
                }
            });
        }
    }

    @Override
    public void onDataNotLoaded(final int num) {
        final SearchContract.UserView view = getmView();
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
