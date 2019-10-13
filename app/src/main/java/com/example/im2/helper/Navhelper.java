package com.example.im2.helper;

import android.content.Context;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



/**
 * 完成对Fragment的调度与重用问题
 * 达到最优的Fragment切换
 * Created by liuzhen on 2019/10/7
 */
public class Navhelper<T> {
    //所有tab
    private final SparseArray<Tab<T>> tabs = new SparseArray();
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final Context context;
    private final OnTabChangedListener<T> listener;
    //选中TAB
    private Tab<T> currentTab;

    public Navhelper(FragmentManager fragmentManager, int containerId, Context context, OnTabChangedListener<T> listener) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.context = context;
        this.listener = listener;
    }

    /**
     * 添加
     * @param menuId
     * @param tab
     */
    public Navhelper<T> add(int menuId, Tab<T> tab){
        tabs.put(menuId, tab);
        return this;
    }

    public Tab<T> getCurrentTab(){
        return currentTab;
    }
    /**
     * 执行点击菜单的操作
     * @param menuId 菜单id
     * @return
     */
    public boolean performClickMenu(int menuId){
        Tab<T> tab = tabs.get(menuId);
        if(tab != null){
            doSelect(tab);
            return true;
        }
        return false;
    }

    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;

        if (currentTab != null){
            oldTab = currentTab;
            if (oldTab == tab) {
                //可以刷新
                notifyReselect(tab);
                return;
            }
        }

        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    /**
     * 切换，进行调度
     * @param newTab
     * @param oldTab
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab){
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (oldTab != null){
            if (oldTab.fragment != null){
                // 移除，加入缓存
                ft.detach(oldTab.fragment);
                //ft.remove(oldTab.fragment);
            }
        }

        if (newTab != null){
            if (newTab.fragment == null){
                androidx.fragment.app.Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null );
                newTab.fragment = fragment;

                ft.add(containerId, fragment, newTab.clx.getName());
            }
            else {
                //ft缓存空间中取出来
                ft.attach(newTab.fragment);
            }
        }
        ft.commit();
        notifyTabSelect(newTab, oldTab);
    }

    private void notifyReselect(Tab<T> tab){
        // TODO 二次点击
    }


    /**
     * 回调我们的监听器
     * @param newTab
     * @param oldTab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab){
        if (listener != null){
            listener.onTabChanged(newTab, oldTab);
        }
    }



    public static class Tab<T>{
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        public Class<?> clx; //继承自fragment
        public T extra;      //额外的字段，用户自己设定需要使用

        Fragment fragment;
    }

    /**
     * 事件处理的回调
     * @param <T>
     */
    public interface OnTabChangedListener<T>{
        void onTabChanged(Tab<T> newTab,Tab<T> oldTab);
    }
}
