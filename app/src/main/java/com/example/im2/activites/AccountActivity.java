package com.example.im2.activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.common.APP.Activity;
import com.example.common.APP.Fragment;
import com.example.im2.R;
import com.example.im2.frags.account.AccountTrigger;
import com.example.im2.frags.account.LoginFragment;
import com.example.im2.frags.account.RegisterFragment;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class AccountActivity extends Activity implements AccountTrigger {
    private Fragment mCurFragment;
    private Fragment loginFragment;
    private Fragment registerFragment;

    @BindView(R.id.im_bg)
    ImageView mBg;
    /**
     * 账户ACTIVITY的入口
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        // 初始化Fragment
        mCurFragment = loginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container_account, mCurFragment)
                .commit();
        // 初始化背景
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop()
                .into(new ViewTarget<ImageView, GlideDrawable>(mBg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Drawable drawable = resource.getCurrent();

                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent), PorterDuff.Mode.SCREEN);

                        this.view.setImageDrawable(drawable);
                    }
                });
    }


    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == loginFragment){
            if (registerFragment == null){
                // 默认为空
                registerFragment = new RegisterFragment();
            }
            fragment = registerFragment;
        }else {
            fragment = loginFragment;
        }

        mCurFragment = fragment;
        // 切换显示
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container_account, fragment)
                .commit();
    }
}
