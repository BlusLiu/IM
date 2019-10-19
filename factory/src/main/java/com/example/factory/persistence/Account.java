package com.example.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.factory.Factory;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 18:59 2019/10/19
 */
public class Account {

    private static final String KET_PUSH_ID = "KET_PUSH_ID";
    private static String pushId;

    private static void save(Context context){
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        sp.edit().putString(KET_PUSH_ID,pushId)
                .apply();
    }

    public static void load(Context context){
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        pushId = sp.getString(KET_PUSH_ID,"");
    }

    public static void setPushId(String pushId){
        Account.pushId = pushId;
        Account.save(Factory.app());
    }

    public static String getPushId(){
        return pushId;
    }

    public static boolean isLogin(){
        if (pushId != null)
            return true;
        return false;
    }
}
