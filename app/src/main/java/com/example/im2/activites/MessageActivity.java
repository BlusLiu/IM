package com.example.im2.activites;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.common.APP.Activity;
import com.example.common.APP.Fragment;
import com.example.factory.model.Author;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.User;
import com.example.im2.MessageRecevier;
import com.example.im2.R;
import com.example.im2.frags.message.ChatGroupFragment;
import com.example.im2.frags.message.ChatUserFragment;

import butterknife.BindView;

public class MessageActivity extends Activity {


    private MessageRecevier messageRecevier;
    private IntentFilter filter;

    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    public static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";
    public static final String KEY_RECEIVER_PORTRAIT = "KEY_RECEIVER_PORTRAIT";
    public static final String KEY_RECEIVER_NAME = "KEY_RECEIVER_NAME";
    private String mReceiverId;
    private String rName;
    private String rPortrait;
    private boolean mIsGroup;
    /**
     * 单人聊天
     * @param context
     * @param author
     */
    public static void show(Context context, Author author){
        if (context == null||author == null|| TextUtils.isEmpty(author.getId()))
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        // 测试user用
        ((User)author).load();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_PORTRAIT, author.getPortrait());
        bundle.putString(KEY_RECEIVER_NAME, author.getName());
        bundle.putString(KEY_RECEIVER_ID, author.getId());
        bundle.putBoolean(KEY_RECEIVER_IS_GROUP, false);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 群聊天
     * @param context
     * @param group
     */
    public static void show(Context context, Group group){
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        filter = new IntentFilter();
        filter.addAction("com.igexin.sdk.action.4qJyoiM47wAcgoGeMwO4G8");
        messageRecevier = new MessageRecevier();
        registerReceiver(messageRecevier, filter);
        rName = bundle.getString(KEY_RECEIVER_NAME);
        rPortrait = bundle.getString(KEY_RECEIVER_PORTRAIT);
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        Fragment fragment;
        if (mIsGroup)
            fragment = new ChatGroupFragment();
        else
            fragment = new ChatUserFragment();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        bundle.putString(KEY_RECEIVER_PORTRAIT,rPortrait);
        bundle.putString(KEY_RECEIVER_NAME, rName);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, fragment)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageRecevier);
    }
}
