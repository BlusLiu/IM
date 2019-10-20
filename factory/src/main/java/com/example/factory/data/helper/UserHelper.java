package com.example.factory.data.helper;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;

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
}
