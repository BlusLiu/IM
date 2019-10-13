package com.example.im2.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.common.APP.Activity;
import com.example.common.APP.Fragment;
import com.example.im2.R;
import com.example.im2.frags.account.UpdateInfoFragment;
import com.yalantis.ucrop.UCrop;

import butterknife.OnClick;

public class AccountActivity extends Activity {

    private Fragment mFragment;
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
