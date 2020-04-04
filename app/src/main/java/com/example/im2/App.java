package com.example.im2;

import com.example.common.APP.Applocation;
import com.example.factory.Factory;
import com.igexin.sdk.PushManager;

/**
 * Created by liuzhen on 2019/10/12
 */
public class App extends Applocation {
    @Override
    public void onCreate() {
        super.onCreate();
        Factory.setup();
        PushManager.getInstance().initialize(this, MessagePushService.class);
        PushManager.getInstance().registerPushIntentService(this, MessageReceiverService.class);
    }
}
