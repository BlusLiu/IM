package com.example.factory.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 18:38 2019/10/26
 */
public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer> extends DiffUtil.Callback {
    private List<T> mOldList, mNewList;

    public DiffUiDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        // 旧的数据大小
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }


    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);

        return beanNew.isSame(beanOld);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);

        return beanNew.isUiContentSame(beanOld);
    }

    public interface UiDataDiffer<T>{
        boolean isSame(T old);

        boolean isUiContentSame(T old);
    }
}
