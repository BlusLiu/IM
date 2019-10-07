package com.example.im2;

import android.media.Image;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.common.APP.Activity;
import com.example.im2.frags.main.ActiveFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.im2_search)
    View search;

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void initWidget() {
        super.initWidget();

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
        Toast.makeText(this,"search", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_action)
    void onAddClick(){
        Toast.makeText(this,"add", Toast.LENGTH_SHORT).show();
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

        if (menuItem.getItemId() == R.id.action_home){
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

        return true;
    }
}
