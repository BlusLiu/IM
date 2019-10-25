package com.example.im2.activites;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.common.APP.Activity;
import com.example.factory.persistence.Account;
import com.example.im2.R;
import com.example.im2.activites.AccountActivity;
import com.example.im2.frags.assist.PermissionsFragment;
import com.example.im2.frags.main.ActiveFragment;
import com.example.im2.frags.main.ContactFragment;
import com.example.im2.frags.main.GroupFragment;
import com.example.im2.helper.Navhelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        Navhelper.OnTabChangedListener<Integer> {

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.im2_search)
    View search;

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private Navhelper<Integer> mNavHelper;

    /**
     * 入口
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }

    // 返回false activity直接finish
    @Override
    protected boolean initArgs(Bundle bundle) {
        if (Account.isComplete()){
            return super.initArgs(bundle);
        }else {
            UserActivity.show(this);
            // 自然销毁
            return false;
        }

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();


        // 初始化
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home,0);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mNavHelper = new Navhelper<Integer>(getSupportFragmentManager(), R.id.lay_container,this,this);
        mNavHelper.add(R.id.action_home,new Navhelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_contact,new Navhelper.Tab<>(ContactFragment.class, R.string.title_contact))
                .add(R.id.action_group,new Navhelper.Tab<>(GroupFragment.class, R.string.title_group));
        //添加监听
        mNavigation.setOnNavigationItemSelectedListener(this);



        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });


    }


    @OnClick(R.id.im2_search)
    void onSerchMenuClick(){
        int type = Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group)? SearchActivity.TYPE_GROUP : SearchActivity.TYPE_USER;

        SearchActivity.show(this,type);
    }

    @OnClick(R.id.btn_action)
    void onActionClick(){
        // 点击判断当前在人还是群
        if (Objects.equals(mNavHelper.getCurrentTab().extra, R.string.title_group)){
            //TODO 打开群创建界面
        }else {
            SearchActivity.show(this,SearchActivity.TYPE_USER);
        }
        //AccountActivity.show(this);
    }



    // 理想情况为复用
    boolean isFirst = true;

    /**
     *
     * @param menuItem
     * @return True代表能处理点击，触发点击事件
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


/*        if (menuItem.getItemId() == R.id.action_home){
            mTitle.setText(R.string.title_home);

            ActiveFragment activeFragment = new ActiveFragment();
            if(isFirst){
                //事务
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.lay_container, activeFragment)
                        .commit();
                //需要移除，否则会无限生成实例
            }

        }
        return true;*/
        return mNavHelper.performClickMenu(menuItem.getItemId());
    }

    /**
     * 利用回调方法，对其tab进行操作
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(Navhelper.Tab<Integer> newTab, Navhelper.Tab<Integer> oldTab) {
        // 从额外字段取出资源id
        mTitle.setText(newTab.extra);

        float transY = 0;
        float rotation = 0;
        if (newTab.extra.equals(R.string.title_home)){
            transY = Ui.dipToPx(getResources(),76);
        }else{
            if (newTab.extra.equals(R.string.title_contact)){
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = -360;
            }
            else {
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = 360;
            }
        }
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();


    }
}
