package com.example.factory.persenter.search;

import com.example.factory.presenter.BasePresenter;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 21:42 2019/10/25
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView> implements SearchContract.Presenter{
    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
