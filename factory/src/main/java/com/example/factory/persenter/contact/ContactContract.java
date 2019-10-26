package com.example.factory.persenter.contact;

import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.User;
import com.example.factory.presenter.BaseContract;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 15:40 2019/10/26
 */
public interface ContactContract {

    interface Presenter extends BaseContract.Presenter{

    }

    interface View extends BaseContract.RecyclerView<Presenter, User>{
        // 不能精确到一条的刷新
        //void onDone(List<User> users);

    }
}
