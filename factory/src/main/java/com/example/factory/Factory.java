package com.example.factory;

import com.example.common.APP.Applocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liuzhen on 2019/10/13
 */
public class Factory {
    private static final Factory instance;
    private final Executor executor;
    private final Gson gson;

    static {
        instance = new Factory();
    }
    private Factory(){
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // TODO 设置一个过滤器，数据库级别的MODEL不进行JSOn转换
                .setExclusionStrategies()
                .create();
    }

    public static Applocation app(){
        return Applocation.getInstance();
    }

    public static void runOnAsync(Runnable runnable){
        instance.executor.execute(runnable);
    }

    public static Gson getGson() {
        return instance.gson;
    }
}
