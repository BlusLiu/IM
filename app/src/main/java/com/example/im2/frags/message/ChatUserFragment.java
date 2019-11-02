package com.example.im2.frags.message;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.widget.Toolbar;

import com.example.common.APP.Fragment;
import com.example.common.widget.PortraitView;
import com.example.factory.model.db.User;
import com.example.factory.persenter.message.ChatContract;
import com.example.factory.persenter.message.ChatUserPresenter;
import com.example.im2.R;
import com.example.im2.activites.PersonalActivity;
import com.google.android.material.appbar.AppBarLayout;

import butterknife.BindView;
import butterknife.OnClick;


public class ChatUserFragment extends ChatFragment<User> implements ChatContract.UserView {
    @BindView(R.id.portrait)
    PortraitView mPortraitView;

    private MenuItem mMenuItem;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        Toolbar toolbar = mToolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_person){
                    onPortraitClick();
                }
                return false;
            }
        });
        mMenuItem = toolbar.getMenu().findItem(R.id.action_person);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        super.onOffsetChanged(appBarLayout, i);

        if (mPortraitView == null || mMenuItem == null)
            return;


        if (i == 0){
            mPortraitView.setVisibility(View.VISIBLE);
            mPortraitView.setScaleX(1);
            mPortraitView.setScaleY(1);
            mPortraitView.setAlpha(1);
            // 隐藏
            mMenuItem.setVisible(false);
            mMenuItem.getIcon().setAlpha(0);
        }else {
            final int max = appBarLayout.getTotalScrollRange();
            float progress = 1 + i / (float) max;
            Log.d("长度", "onOffsetChanged: "+progress);

            if (i >= max){
                mPortraitView.setVisibility(View.INVISIBLE);
                mPortraitView.setScaleX(0);
                mPortraitView.setScaleY(0);
                mPortraitView.setAlpha(0);

                mMenuItem.setVisible(true);
                mMenuItem.getIcon().setAlpha(255);
            }else {
                mPortraitView.setVisibility(View.VISIBLE);
                mPortraitView.setScaleX(progress);
                mPortraitView.setScaleY(progress);
                mPortraitView.setAlpha(progress);

                mMenuItem.setVisible(true);
                mMenuItem.getIcon().setAlpha(255 - (int)(255*progress));
            }
        }
    }

    @OnClick(R.id.portrait)
    void onPortraitClick(){
        PersonalActivity.show(getContext(), mReceiverId);
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatUserPresenter(this, mReceiverId);
    }

    // 对方信息的初始化
    @Override
    public void onInit(User user) {

    }
}
