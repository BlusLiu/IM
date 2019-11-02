package com.example.im2.frags.message;

import com.example.common.APP.Fragment;
import com.example.factory.model.db.Group;
import com.example.factory.persenter.message.ChatContract;
import com.example.im2.R;


public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }


    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}
