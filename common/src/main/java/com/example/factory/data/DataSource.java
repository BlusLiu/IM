package com.example.factory.data;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 13:55 2019/10/19
 */
public interface DataSource {

    interface Callback<T> extends SucceedCallback<T>, FailedCallback{

    }

    /**
     * 只关注成功的接口
     * @param <T>
     */
    interface SucceedCallback<T>{
        void onDataLoaded(T t);
    }


    /**
     * 只关注失败的接口
     * @param <T>
     */
    interface FailedCallback{
        void onDataNotLoaded(int num);
    }
}
