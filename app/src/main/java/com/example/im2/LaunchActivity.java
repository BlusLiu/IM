package com.example.im2;

import android.os.Bundle;

import com.example.common.APP.Activity;
import com.example.im2.activites.MainActivity;
import com.example.im2.frags.assist.PermissionsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())){
            MainActivity.show(this);

            finish();
        }
    }
}
