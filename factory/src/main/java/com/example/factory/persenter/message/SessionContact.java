package com.example.factory.persenter.message;

import com.example.factory.model.db.Session;
import com.example.factory.presenter.BaseContract;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 16:47 2020/4/4
 */
public interface SessionContact {
    interface Presenter extends BaseContract.Presenter{


    }

    interface View extends BaseContract.RecyclerView<Presenter, Session>{

    }
}
