package com.example.factory.presenter;

import androidx.recyclerview.widget.DiffUtil;

import com.example.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 15:42 2019/10/27
 */
public class BaseRecyclerPresenter<ViewModel ,View extends BaseContract.RecyclerView> extends BasePresenter<View>{
    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    protected void refreshData(final List<ViewModel> dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getmView();
                if (view == null)
                    return;
                RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();
                adapter.replace(dataList);
                view.onAdapterDataChange();
            }
        });
    }

    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<ViewModel> dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                refreshDataOnUiThread(diffResult, dataList);
            }
        });
    }

    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult, final List<ViewModel> dataList){
        View view = getmView();
        if (view == null)
            return;
        RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);

        view.onAdapterDataChange();

        diffResult.dispatchUpdatesTo(adapter);
    }
}
