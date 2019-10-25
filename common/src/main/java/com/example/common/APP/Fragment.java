package com.example.common.APP;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuzhen on 2019/10/4
 */
public abstract class Fragment extends androidx.fragment.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnbinder;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRoot == null){
            int layId = getContentLayoutId();
            //初始化当前根布局
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        }else {
            if (mRoot.getParent() != null){
                //
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        initData();
        //return super.onCreateView(inflater, container, savedInstanceState);
        return mRoot;
    }

    /**
     * 当前界面资源文件的ID
     * @return id
     */
    protected abstract int getContentLayoutId();

    protected void initWidget(View root){
        mRootUnbinder = ButterKnife.bind(this, root);

    }

    protected void initData(){

    }

    protected void initArgs(Bundle bundle){

    }

    /**
     * 返回按键触发时调用
     * @return TRUE代表已经处理返回逻辑，activity不用自己finish
     */
    public boolean onBackPressed(){
        return false;
    }
}
