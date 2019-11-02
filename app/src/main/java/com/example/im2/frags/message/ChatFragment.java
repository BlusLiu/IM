package com.example.im2.frags.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.common.APP.Fragment;
import com.example.common.APP.PresenterFragment;
import com.example.common.widget.PortraitView;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.persenter.message.ChatContract;
import com.example.factory.persistence.Account;
import com.example.im2.R;
import com.example.im2.activites.MessageActivity;
import com.google.android.material.appbar.AppBarLayout;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 15:10 2019/11/1
 */
public abstract class ChatFragment<InitModel>
        extends PresenterFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener,
        ChatContract.View<InitModel> {
    protected String mReceiverId;
    protected Adapter mAdapter;



    @BindView(R.id.toolbar_test)
    Toolbar mToolbar;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.edit_content)
    EditText mContent;

    @BindView(R.id.btn_submit)
    View mSubmit;




    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);

    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initToolbar();
        initAppbar();
        initEditContent();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void initToolbar(){
        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void initAppbar(){
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

        mPresenter.start();
    }

    private void initEditContent(){
        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 其实这一句可以不用
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                mSubmit.setActivated(needSendMsg);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

    }

    @OnClick(R.id.btn_face)
    void onFaceClick(){

    }

    @OnClick(R.id.btn_record)
    void onRecordClick(){

    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        if (mSubmit.isActivated()){
            String content = mContent.getText().toString();
            mContent.setText("");
            mPresenter.pushText(content);
        }
    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {
        // 界面没有占位布局
    }

    private class Adapter extends RecyclerAdapter<Message>{

        @Override
        protected int getItemViewType(int position, Message message) {

            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());

            switch (message.getType()){
                case Message.TYPE_STR:
                    return isRight?R.layout.cell_chat_text_right:R.layout.cell_chat_text_left;
                case Message.TYPE_AUDIO:
                    return isRight?R.layout.cell_chat_audio_right:R.layout.cell_chat_audio_left;

                case Message.TYPE_PIC:
                    return isRight?R.layout.cell_chat_pic_right:R.layout.cell_chat_pic_left;

            }
            return 0;
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType){
                case R.layout.cell_chat_text_right :
                case R.layout.cell_chat_text_left :
                    return new TextHolder(root);

                case R.layout.cell_chat_audio_right :
                case R.layout.cell_chat_audio_left :
                    return new AudioHolder(root);

                case R.layout.cell_chat_pic_right :
                case R.layout.cell_chat_pic_left :
                    return new PicHolder(root);

                    default:return new TextHolder(root);
            }
        }
    }

    // holder基类
    class  BaseHolder extends RecyclerAdapter.ViewHolder<Message>{

        @BindView(R.id.portrait)
        PortraitView mPortrait;

        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            sender.load();

            mPortrait.setup(Glide.with(getContext()), sender);

            if (mLoading != null){
                int status = message.getStatus();
                if (status == Message.STATUS_DONE){
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();

                }else if (status == Message.STATUS_FAILED){
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.stop();
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));

                }
                mPortrait.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @OnClick(R.id.portrait)
        void onRePushClick(){
            if (mLoading != null){

            }
        }
    }

    class  TextHolder extends BaseHolder{
        @BindView(R.id.txt_content)
        TextView mContent;

        public TextHolder(View itemView){
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            mContent.setText(message.getContent());
        }
    }
    class  PicHolder extends BaseHolder{
        @BindView(R.id.txt_content)
        TextView mContent;

        public PicHolder(View itemView){
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {

        }
    }
    class  AudioHolder extends BaseHolder{
        @BindView(R.id.txt_content)
        TextView mContent;

        public AudioHolder(View itemView){
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {

        }
    }
}
