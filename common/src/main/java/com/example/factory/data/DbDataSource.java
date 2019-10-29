package com.example.factory.data;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 11:05 2019/10/29
 */
public interface DbDataSource<Data> extends DataSource {
    /**
     *
     * @param callback 一般回调到presenter
     */
    void load(SucceedCallback<List<Data>> callback);
}
