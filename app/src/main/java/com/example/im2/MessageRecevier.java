package com.example.im2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
    private static final String TAG = MessageRecevier.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;

        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:{

                Log.i(TAG, "GET_CLIENTID: "+bundle.toString());
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA:{
                // 常规消息送达

                byte[] payload = bundle.getByteArray("payload");
                if (payload != null){
                    String message = new String(payload);
                    Log.i(TAG, "GET_MSG_DATA: "+message);
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
        Account.setPushId(cid);
        if (Account.isLogin()){
            AccountHelper.bindPush(null);
        }
    }

    private void onMessageArrived(String message){
        Factory.dispatchPush(message);
    }
}
