package com.example.factory.persenter.message;

import androidx.recyclerview.widget.DiffUtil;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.data.message.SessionDataSoure;
import com.example.factory.data.message.SessionRepository;
import com.example.factory.model.db.Session;
import com.example.factory.persenter.BaseSourcePresenter;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 17:27 2020/4/4
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSoure, SessionContact.View> implements SessionContact.Presenter{

    public SessionPresenter(SessionContact.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContact.View view = getmView();
        if(view == null){
            return;
        }
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, sessions);
    }
}
