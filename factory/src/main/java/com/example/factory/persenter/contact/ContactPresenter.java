package com.example.factory.persenter.contact;

import androidx.annotation.NonNull;

import com.example.factory.data.DataSource;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BasePresenter;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 15:58 2019/10/26
 */
public class ContactPresenter extends BasePresenter<ContactContract.View> implements ContactContract.Presenter {
    public ContactPresenter(ContactContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        // 依赖问题
                        getmView().getRecyclerAdapter().replace(tResult);
                        getmView().onAdapterDataChange();

                    }
                })
                // 最后一步也要的
                .execute();

        // 加载网络数据
        UserHelper.refreshContacts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataNotLoaded(int num) {

            }

            @Override
            public void onDataLoaded(List<UserCard> userCards) {

                final List<User> users = new ArrayList<>();

                // 可以进行事物优化
                for (UserCard userCard : userCards) {
                    users.add(userCard.build());
                    userCard.build().save();
                }

                getmView().getRecyclerAdapter().replace(users);
                getmView().onAdapterDataChange();
            }
        });

        // TODO
        //  1.关注后没有刷新
        //  2.全局刷新 

    }
}
