package com.example.im2.activites;


import android.content.Intent;

import com.example.common.APP.Activity;


import com.example.common.APP.Fragment;
import com.example.im2.R;
import com.example.im2.frags.user.UpdateInfoFragment;

public class UserActivity extends Activity {
    private Fragment mFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container_account,mFragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFragment.onActivityResult(requestCode, resultCode, data);
    }
}
