package com.example.im2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.persistence.Account;
import com.igexin.sdk.PushConsts;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 19:32 2019/10/19
 */
public class MessageRecevier extends BroadcastReceiver {
    //private static final String TAG = MessageRecevier.class.getSimpleName();
    private static final String TAG = "MessageRecevier";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;

        Bundle bundle = intent.getExtras();
        Log.e(TAG, "onReceive: 收到消息啦！！！！");
        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:{

                Log.e(TAG, "onReceive: 这是id "+bundle.toString());
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA:{
                // 常规消息送达

                byte[] payload = bundle.getByteArray("payload");
                if (payload != null){
                    String message = new String(payload);
                    Log.e(TAG, "onReceive: 这是消息"+message);
                    onMessageArrived(message);
                }
                break;
            }
            default:{
                Log.i(TAG, "Other");
                break;
            }
        }
    }

    private void onClientInit(String cid){
        // 设置设备ID
        Account.setPushId(cid);
        // 没有登陆是不能绑定的
        if (Account.isLogin()){
            AccountHelper.bindPush(null);
        }
    }

    private void onMessageArrived(String message){
        Factory.dispatchPush(message);
    }
}
