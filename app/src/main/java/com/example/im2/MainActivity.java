package com.example.im2;

import android.media.Image;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.common.APP.Activity;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity{

    @BindView(R.id.im2_search)
    View search;

    @BindView(R.id.appbar)
    View mLayAppbar;

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
}
