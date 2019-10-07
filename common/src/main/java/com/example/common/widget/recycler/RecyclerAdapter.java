package com.example.common.widget.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by liuzhen on 2019/10/4
 */
public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, AdapterCallback<Data> {
    private List<Data> mDataList = new ArrayList();
    private AdapterListener<Data> mListener;

    /**
     * 构造函数模块
     * @param listener
     */
    public RecyclerAdapter(AdapterListener<Data> listener){
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener){
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * 创建一个ViewHolder
     * @param parent RecyclerView
     * @param viewType 界面的类型，约定为XML布局的ID
     * @return
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 得到LayoutInflater用于把xml初始化为view(对象)
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 把xml id为viewType的文件初始化为一个root view
        View root = inflater.inflate(viewType, parent, false);
        // 通过子类必须实现的方法， 得到一个viewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        root.setTag(R.id.tag_recycler_holder, holder);

        // 设置点击事件
        root.setOnClickListener(this);
        //root.setOnLongClickListener(this);



        // 进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        holder.callback = this;
        return null;
    }


    /**
     * 创建viewholder
     * @param root
     * @param viewType
     * @return
     */
    protected abstract  ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定数据到一个holder上
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        holder.bind(data);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void add(Data data){
        mDataList.add(data);
        //notifyDataSetChanged();
        notifyItemInserted(mDataList.size() - 1);
    }

    public void add(Data... dataList){
        if (dataList != null && dataList.length > 0){
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeChanged(startPos, dataList.length);
        }
    }

    public void add(Collection<Data> dataList){
        if (dataList != null && dataList.size() > 0){
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeChanged(startPos, dataList.size());
        }
    }

    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void replace(Collection<Data> dataList){
        mDataList.clear();
        if (dataList != null && dataList.size() > 0){
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null){
            int pos = viewHolder.getAdapterPosition();
            // 回调方法
            this.mListener.onItenClick(viewHolder, mDataList.get(pos));
        }
    }

    public void setmListener(AdapterListener<Data> adapterListener){
        this.mListener = adapterListener;
    }

    /**
     * 自定义监听器
     * @param <Data> 泛型
     */
    public interface AdapterListener<Data>{

        void onItenClick(RecyclerAdapter.ViewHolder holder, Data data);


    }

    public abstract static class ViewHolder<Data> extends RecyclerView.ViewHolder{
        protected Data mData;
        private Unbinder unbinder;
        private AdapterCallback<Data> callback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         * @param data
         */
        void bind(Data data){
            this.mData = data;
        }

        /**
         * 触发的回调，必须实现
         * @param data
         */
        protected abstract void onBind(Data data);

        /**
         * holde自己对自己的data更新
         * @param data
         */
        public void updateData(Data data) {
            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }


}
