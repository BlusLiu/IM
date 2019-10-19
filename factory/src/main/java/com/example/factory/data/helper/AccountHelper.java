package com.example.factory.data.helper;

import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.DataSource;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.User;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 13:30 2019/10/19
 */
public class AccountHelper {

    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        RemoteService service = Network.getRetroFit().create(RemoteService.class);
        // 得到一个call
        Call<RspModel<AccountRspModel>> call = service.accountRefister(model);
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
                // 请求成功
                RspModel<AccountRspModel> rspModel = response.body();
                //Log.d("user", "onResponse: "+rspModel.getResult().getUser().toString());
                if (rspModel.success()){
                    AccountRspModel accountRspModel = rspModel.getResult();

                    if (accountRspModel.isBind()){
                        User user = accountRspModel.getUser();
                        // TODO 缓存绑定
                        callback.onDataLoaded(user);
                    }else {
                        bindPush(callback);

                    }

                }else {
                    // TODO 服务器错误callback.onDataNotLoaded(R.String.data_rsp_error_service);
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                callback.onDataNotLoaded(R.string.data_network_error);
            }
        });

        // 我吐了，开个线程忘记跑了...
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    //callback.onDataNotLoaded(R.string.data_network_error);
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                callback.onDataNotLoaded(R.string.data_rsp_error_parameters);
//            }
//        }.run();
    }

    /**
     * 对设备id绑定
     * @param callback
     */
    public static void bindPush(final DataSource.Callback<User> callback){
        // TODO 绑定
        callback.onDataNotLoaded(R.string.app_name);
    }
}
