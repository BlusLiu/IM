package com.example.im2.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.common.APP.Activity;
import com.example.factory.model.Author;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.im2.R;

public class MessageActivity extends Activity {
    public static void show(Context context, Author author){
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

}
