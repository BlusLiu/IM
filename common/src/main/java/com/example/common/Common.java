package com.example.common;

public class Common {
    //int id = R.drawable.default_portrait;

    /**
     * 持久化参数
     * 用于配置
     */
    public interface Constance{
        // phone正则
        String REGEX_MOBILE = "[1][3,4,5,6,7,8][0-9]{9}$";
        String API_URL_308 = "192.168.1.102:8889/api/";
    }
}
