package com.example.factory.data.user;

import androidx.annotation.NonNull;

import com.example.factory.data.DataSource;
import com.example.factory.data.helper.DbHelper;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 19:05 2019/10/28
 */
public class ContactRepository implements ContactDataSource,
        QueryTransaction.QueryResultListCallback<User>,
        DbHelper.ChangedListener<User> {
    private DataSource.SucceedCallback<List<User>> callback;

    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        this.callback = callback;

        DbHelper.addChangedListener(User.class, this);
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                // 最后一步也要的
                .execute();
    }

    @Override
    public void dispose() {
        this.callback = null;
        DbHelper.removeChangedListener(User.class, this);
    }


    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
//        if (callback != null){
//            callback.onDataLoaded(tResult);
//        }
        if (tResult.size() == 0){
            users.clear();
            notifyDataChange();
            return;
        }

        User[] users = tResult.toArray(new User[0]);

        onDataSave(users);
    }

    @Override
    public void onDataSave(User... list) {
        boolean isChanged = false;
        for (User user : list) {
            if (isRequired(user)){
                insertOrUpdate(user);
                isChanged = true;
            }
        }

        if (isChanged)
            notifyDataChange();
    }

    @Override
    public void onDataDelete(User... list) {
        boolean isChanged = false;
        for (User user : list) {
            if (users.remove(user))
                isChanged = true;
        }
        if (isChanged)
            notifyDataChange();
    }

    List<User> users = new LinkedList<>();
    private void insertOrUpdate(User user){
        // 不能来判断？boolean index = users.contains(user);

        int index = indexOf(user);
        if (index >= 0){
            replace(index, user);
        }else insert(user);

    }

    private void insert(User user){
        users.add(user);
    }

    private void replace(int index, User user){
        users.remove(index);
        users.add(index, user);
    }

    private int indexOf(User user){
        int index = -1;
        for (User user1 : users) {
            index++;
            if (user1.isSame(user)){
                return index;
            }
        }
        return -1;
    }

    private void notifyDataChange(){
        if (callback != null)
            callback.onDataLoaded(users);
    }

    private boolean isRequired(User user){
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }
}
