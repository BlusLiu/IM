package com.example.factory.persenter.search;

import com.example.factory.presenter.BasePresenter;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 21:43 2019/10/25
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView> implements SearchContract.Presenter {

    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
