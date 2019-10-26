package com.example.factory.persenter.search;

import com.example.factory.data.DataSource;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.BasePresenter;

import java.util.List;

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
        start();

        //这里可以优化
        //Call call = searchCall;
        //UserHelper.search(content, this);
    }


}
