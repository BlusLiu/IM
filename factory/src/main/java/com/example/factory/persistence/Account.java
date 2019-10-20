package com.example.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.factory.Factory;
import com.example.factory.model.api.account.AccountRspModel;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 18:59 2019/10/19
 */
public class Account {

    private static final String KET_PUSH_ID = "KET_PUSH_ID";
    private static final String KET_IS_BIND= "KET_IS_BIND";
    private static final String KET_TOKEN= "KET_TOKEN";
    private static final String KET_USER_ID= "KET_USER_ID";
    private static final String KET_ACCOUNT= "KET_ACCOUNT";


    private static String pushId;
    private static boolean isBind;

    private static String token;
    private static String userId;
    // 登陆的账户
    private static String account;

    private static void save(Context context){
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        sp.edit().putString(KET_PUSH_ID,pushId)
                .putBoolean(KET_IS_BIND,isBind)
                .putString(KET_TOKEN,token)
                .putString(KET_USER_ID,userId)
                .putString(KET_ACCOUNT,account)
                .apply();
    }

    public static void load(Context context){
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        pushId = sp.getString(KET_PUSH_ID,"");
        isBind = sp.getBoolean(KET_IS_BIND,false);
        token = sp.getString(KET_TOKEN,"");
        userId =sp.getString(KET_USER_ID,"");
        account = sp.getString(KET_ACCOUNT,"");
    }

    public static void setPushId(String pushId){
        Account.pushId = pushId;
        Log.d("pushid", "setPushId: "+pushId);
        Account.save(Factory.app());
    }

    public static String getPushId(){
        return pushId;
    }

    public static boolean isLogin(){
        return !TextUtils.isEmpty(userId)
                && !TextUtils.isEmpty(token);
    }

    public static boolean isComplete(){
        return isLogin();
    }

    public static Boolean isBind(){
        return isBind;
    }

    public static void setBind(boolean isBind){
        Account.isBind = isBind;
        Account.save(Factory.app());
    }

    public static void login(AccountRspModel model){
        // 存储用户的token， id， 方便从数据库中查询
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();
        save(Factory.app());
    }

    public static User getUser(){
        return TextUtils.isEmpty(userId)? new User():
                SQLite.select().from(User.class)
                        .where(User_Table.id.eq(userId))
                        .querySingle();
    }

    public static String getToken(){
        return token;
    }
}
