package com.example.factory.net;

import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.db.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 15:19 2019/10/19
 */
public interface RemoteService {
    /**
     * 网络请求一个注册接口
     * @param model
     * @return
     */
    @POST
    Call<RspModel<AccountRspModel>> accountRefister(@Body RegisterModel model);
}
