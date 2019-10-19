package com.example.im2;

import com.example.common.APP.Applocation;
import com.example.factory.Factory;

/**
 * Created by liuzhen on 2019/10/12
 */
public class App extends Applocation {
    @Override
    public void onCreate() {
        super.onCreate();

        Factory.setup();

        
    }
}
