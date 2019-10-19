package com.example.im2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.common.APP.Activity;
import com.example.factory.persistence.Account;
import com.example.im2.activites.MainActivity;
import com.example.im2.frags.assist.PermissionsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

/**
 * 初始化
 */
public class LaunchActivity extends Activity {

    private ColorDrawable mColorDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        View root = findViewById(R.id.activity_launch);

        int color = UiCompat.getColor(getResources(),R.color.colorPrimary);

        ColorDrawable drawable = new ColorDrawable(color);

        root.setBackground(drawable);

        mColorDrawable = drawable;

    }

    @Override
    protected void initData() {
        super.initData();

        // 动画进入到50%，等待PushId获取到
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        });
    }

    private void waitPushReceiverId(){
        if (!TextUtils.isEmpty(Account.getPushId())){
            skip();
            return;
        }

        getWindow().getDecorView()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waitPushReceiverId();
                    }
                }, 500);
    }


    // 跳转之前完成剩下的50
    private void skip(){
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });

    }

    private void reallySkip(){
        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void startAnim(float endProgress, final  Runnable endCallback){

        int finalColor = Resource.Color.WHITE;

        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int)evaluator.evaluate(endProgress, mColorDrawable.getColor(), finalColor);

        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property,evaluator, endColor);
        valueAnimator.setDuration(2500);
        valueAnimator.setIntValues(mColorDrawable.getColor(), endColor);

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    private Property<LaunchActivity, Object> property = new Property<LaunchActivity, Object>(Object.class, "Color") {
        @Override
        public void set(LaunchActivity object, Object value) {
            object.mColorDrawable.setColor((Integer) value);
        }

        @Override
        public Object get(LaunchActivity object) {
            return object.mColorDrawable.getColor();
        }
    };
}
