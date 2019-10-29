package com.example.factory.data;

import androidx.annotation.NonNull;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.model.db.BaseDbModel;
import com.example.utils.CollectionUtil;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 11:04 2019/10/29
 */
public abstract class BaseDbRepository<Data extends BaseDbModel<Data>> implements DbDataSource<Data>,
        DbHelper.ChangedListener<Data>,
        QueryTransaction.QueryResultListCallback<Data> {
    // 和presenter交互的回调
    private SucceedCallback<List<Data>> callback;
    private List<Data> dataList = new LinkedList<>();
    private Class<Data> dataClass;

    public BaseDbRepository(){
        Type[] types = Reflector.getActualTypeArguments(BaseDbRepository.class, this.getClass());

        dataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SucceedCallback<List<Data>> callback) {
        this.callback = callback;
        registerDbChangedListener();
    }

    @Override
    public void dispose() {
        this.callback = null;
        DbHelper.removeChangedListener(dataClass, this);
        dataList.clear();
    }

    @Override
    public void onDataSave(Data... list) {
        boolean isChanged = false;
        for (Data data : list) {
            if (isRequired(data)){
                insertOrUpdate(data);
                isChanged = true;
            }
        }

        if (isChanged)
            notifyDataChange();
    }

    @Override
    public void onDataDelete(Data... list) {
        boolean isChanged = false;
        for (Data data : list) {
            if (dataList.remove(data))
                isChanged = true;
        }
        if (isChanged)
            notifyDataChange();
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        if (tResult.size() == 0){
            dataList.clear();
            notifyDataChange();
            return;
        }

        // Data[] datas = tResult.toArray(new Data[0]);  不行
        Data[] datas = CollectionUtil.toArray(tResult, dataClass);

        onDataSave(datas);
    }

    private void notifyDataChange(){
        SucceedCallback<List<Data>> callback = this.callback;
        if (callback != null){
            callback.onDataLoaded(dataList);
        }

    }


    protected void insertOrUpdate(Data data){
        // 不能来判断？boolean index = users.contains(user);

        int index = indexOf(data);
        if (index >= 0){
            replace(index, data);
        }else insert(data);

    }

    protected void insert(Data data){
        dataList.add(data);
    }

    protected void replace(int index, Data data){
        dataList.remove(index);
        dataList.add(index, data);
    }

    protected int indexOf(Data newData){
        int index = -1;
        for (Data data : dataList) {
            index++;
            if (data.isSame(newData)){
                return index;
            }
        }
        return -1;
    }

    protected abstract boolean isRequired(Data data);

    protected void registerDbChangedListener(){
        DbHelper.addChangedListener(dataClass, this);
    }
}
