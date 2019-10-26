package com.example.factory.data.helper;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.example.factory.persenter.contact.FollowPresenter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 18:53 2019/10/20
 */
public class UserHelper {
    public static void update(UserUpdateModel model,final DataSource.Callback<UserCard> callback){

        RemoteService service = Network.remote();
        // 得到一个call
        Call<RspModel<UserCard>> call = service.userUpdate(model);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()){
                    UserCard card = rspModel.getResult();
                    // 数据库的存储
                    User user = card.build();
                    user.save();
                    // 返回成功
                    callback.onDataLoaded(card);

                }else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotLoaded(R.string.data_network_error);
            }
        });
    }

    public static void search(String name,final DataSource.Callback<List<UserCard>> callback){

        RemoteService service = Network.remote();
        // 得到一个call
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);

        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()){
                    List<UserCard> cards = rspModel.getResult();
                    // 返回成功
                    callback.onDataLoaded(cards);

                }else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotLoaded(R.string.data_network_error);
            }
        });
    }


    public static void follow(String id, final DataSource.Callback<UserCard> callBack) {
        RemoteService service = Network.remote();
        // 得到一个call
        Call<RspModel<UserCard>> call = service.userFollow(id);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()){
                    UserCard userCard = rspModel.getResult();
                    User user = userCard.build();
                    user.save();

                    // TODO 通知联系人
                    callBack.onDataLoaded(userCard);
                }else {
                    Factory.decodeRspCode(rspModel, callBack);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callBack.onDataNotLoaded(R.string.data_network_error);
            }
        });
    }

    public static void refreshContacts(final DataSource.Callback<List<UserCard>> callback){

        RemoteService service = Network.remote();
        // 得到一个call
        Call<RspModel<List<UserCard>>> call = service.userContact();

        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()){
                    List<UserCard> cards = rspModel.getResult();
                    // 返回成功
                    callback.onDataLoaded(cards);

                }else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotLoaded(R.string.data_network_error);
            }
        });
    }

    public static User searchFirstOfLoc(String id){
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }


    public static User searchFirstOfNet(String id){
        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();

            if (card != null) {

                card.build().save();
                // TODO 数据库刷新但是没有更改
                return card.build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    // 搜索一个用户
    public static User search(String id){
        User user = searchFirstOfLoc(id);
        if (user == null){
            return searchFirstOfNet(id);
        }
        return user;
    }
}
