package com.example.common.widget.recycler;

/**
 * Created by liuzhen on 2019/10/4
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
