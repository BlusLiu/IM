package com.example.factory.net;

import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.api.account.LoginModel;
import com.example.factory.model.api.account.RegisterModel;
import com.example.factory.model.api.message.MsgCreateModel;
import com.example.factory.model.api.user.UserUpdateModel;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRefister(@Body RegisterModel model);

    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    // 用户更新
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String id);

    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContact();

    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    // 发送消息
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);
}
