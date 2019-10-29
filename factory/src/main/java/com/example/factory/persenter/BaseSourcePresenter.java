package com.example.factory.persenter;

import com.example.factory.data.DataSource;
import com.example.factory.data.DbDataSource;
import com.example.factory.presenter.BaseContract;
import com.example.factory.presenter.BasePresenter;
import com.example.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 13:11 2019/10/29
 */
public abstract class BaseSourcePresenter<Data, ViewModel,
        Source extends DbDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SucceedCallback<List<Data>> {
    protected Source mSource;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        mSource.load(this);
    }

    @Override
    public void destory() {
        super.destory();
        mSource.dispose();
        mSource = null;
    }
}
