package com.example.factory.net;

import com.example.common.Common;
import com.example.factory.Factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 14:54 2019/10/19
 */
public class Network {

    // 构建一个retrofit
    public static Retrofit getRetroFit(){
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit.Builder builder = new Retrofit.Builder();

        Retrofit retrofit = builder.baseUrl(Common.Constance.API_URL_308)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

        return retrofit;
    }
}
