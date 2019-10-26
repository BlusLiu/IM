package com.example.common.APP;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.common.R2;
import com.example.common.widget.convention.PlaceHolderView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by liuzhen on 2019/10/4
 */
public abstract class Activity extends AppCompatActivity {
    protected PlaceHolderView mPlaceHolderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (initArgs(getIntent().getExtras())){
            int layId = getContentLayoutId();
            setContentView(layId);
            initBefore();
            initWidget();
            initData();
        }
    }

    // 在所有操作初始化之前
    protected void initBefore(){

    }

    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 得到当前界面的资源文件ID
     * @return ID
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(){
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        List<androidx.fragment.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null  && fragments.size() > 0){
            for (Fragment fragment : fragments){
                //是我们自己包的Fragment
                if (fragment instanceof com.example.common.APP.Fragment){
                    //如果有直接return
                    if (((com.example.common.APP.Fragment) fragment).onBackPressed()){
                        return ;
                    }

                }
            }
        }


        super.onBackPressed();
        finish();
    }
    public void setmPlaceHolderView(PlaceHolderView view){
        this.mPlaceHolderView = view;
    }

}
