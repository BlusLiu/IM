package com.example.factory.persenter.search;

import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.BaseContract;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 21:38 2019/10/25
 */
public interface SearchContract {
    interface Presenter extends BaseContract.Presenter{
        void search(String content);
    }

    interface UserView extends BaseContract.View<Presenter>{
        void onSearchDone(List<UserCard> userCards);
    }

    interface GroupView extends BaseContract.View<Presenter>{
        void onSearchDone(List<GroupCard> groupCards);
    }
}
