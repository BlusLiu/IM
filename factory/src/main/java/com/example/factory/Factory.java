package com.example.factory;

import com.example.common.APP.Applocation;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by liuzhen on 2019/10/13
 */
public class Factory {
    private static final Factory instance;
    private final Executor executor;

    static {
        instance = new Factory();
    }
    private Factory(){
        executor = Executors.newFixedThreadPool(4);
    }

    public static Applocation app(){
        return Applocation.getInstance();
    }

    public static void runOnAsync(Runnable runnable){
        instance.executor.execute(runnable);
    }
}
