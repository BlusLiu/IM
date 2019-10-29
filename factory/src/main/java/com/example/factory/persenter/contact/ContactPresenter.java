package com.example.factory.persenter.contact;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.data.DataSource;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.user.ContactDataSource;
import com.example.factory.data.user.ContactRepository;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.persenter.BaseSourcePresenter;
import com.example.factory.persistence.Account;
import com.example.factory.presenter.BasePresenter;
import com.example.factory.presenter.BaseRecyclerPresenter;
import com.example.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 15:58 2019/10/26
 */
public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View>
        implements
        ContactContract.Presenter ,
        DataSource.SucceedCallback<List<User>>{
    public ContactPresenter(ContactContract.View view) {
        super(new ContactRepository(), view);

    }

    @Override
    public void start() {
        super.start();
        // 不加载本地的也有对比，但无用户友好性
        mSource.load(this);
        // 加载网络数据
        UserHelper.refreshContacts();
/*        SQLite.select()
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
                .execute();*/



        // TODO
        //  1.关注后没有刷新
        //  2.全局刷新
        //  3.上述本地和网络异步刷新可能会出现冲突的问题
        //  4.如何识别本地已经存在

        /*new DataSource.Callback<List<UserCard>>() {
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

                diff(getmView().getRecyclerAdapter().getItems(),users);

                //getmView().getRecyclerAdapter().replace(users);


            }
        }*/
    }


    // 保证这里是子线程
    @Override
    public void onDataLoaded(List<User> users) {
        final ContactContract.View view = getmView();
        if (view == null)
            return;
        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();

        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        refreshData(result, users);
    }

//    private void diff(List<User> oldList, List<User> newList){
//        DiffUtil.Callback callback = new DiffUiDataCallback<User>(oldList, newList);
//        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
//
//        getmView().getRecyclerAdapter().replace(newList);
//
//        result.dispatchUpdatesTo(getmView().getRecyclerAdapter());
//        getmView().onAdapterDataChange();
//    }

}
