package com.example.im2.frags.main;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.APP.PresenterFragment;
import com.example.common.widget.EmptyView;
import com.example.common.widget.PortraitView;

import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.persenter.contact.ContactContract;
import com.example.factory.persenter.contact.ContactPresenter;
import com.example.factory.persistence.Account;
import com.example.im2.MessageRecevier;
import com.example.im2.R;
import com.example.im2.activites.MessageActivity;
import com.example.im2.activites.PersonalActivity;
import com.example.im2.frags.search.SearchUserFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends PresenterFragment<ContactContract.Presenter> implements ContactContract.View {

    @BindView(R.id.empty)
    EmptyView emptyView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private RecyclerAdapter<User> mAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new RecyclerAdapter<User>() {

            @Override
            protected int getItemViewType(int position, User user) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });
        mAdapter.setmListener(new RecyclerAdapter.AdapterListener<User>() {
            @Override
            public void onItenClick(RecyclerAdapter.ViewHolder holder, User user) {
                MessageActivity.show(getContext(), user);
            }
        });
        onFirstInit();
        emptyView.bind(recyclerView);
        setmPlaceHolderView(emptyView);


    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User>{
        @BindView(R.id.im_portrait)
        PortraitView portrait;

        @BindView(R.id.txt_desc)
        TextView desc;

        @BindView(R.id.txt_name)
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            portrait.setup(Glide.with(getContext()), user);
            name.setText(user.getName());
            desc.setText(user.getDesc());
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick(){
            PersonalActivity.show(getContext(), mData.getId());
        }
    }



}
