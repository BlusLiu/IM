package com.example.factory.persistence;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 18:59 2019/10/19
 */
public class Account {
    private static String pushId;

    public static void setPushId(String pushId){
        Account.pushId = pushId;
    }

    public static String getPushId(){
        return pushId;
    }
}
