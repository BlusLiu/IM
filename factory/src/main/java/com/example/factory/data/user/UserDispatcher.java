package com.example.factory.data.user;

import android.text.TextUtils;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 10:57 2019/10/27
 */
public class UserDispatcher implements UserCenter {
    private static UserCenter instance;
    // 单线程池
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static UserCenter instance(){
        if (instance == null){
            // 同步线程
            synchronized (UserDispatcher.class){
                if (instance == null)
                    instance = new UserDispatcher();
            }
        }
        return instance;
    }
    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0)
            return;
        executor.execute(new UserCardHandler(cards));

    }

    private class UserCardHandler implements Runnable{
        private final UserCard[] cards;

        private UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {

            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                if (card == null || TextUtils.isEmpty(card.getId()))
                    continue;
                users.add(card.build());
            }

            DbHelper.save(User.class, users.toArray(new User[0]));
        }
    }
}
