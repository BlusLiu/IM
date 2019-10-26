package com.example.im2.frags.search;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.common.APP.Fragment;
import com.example.common.APP.PresenterFragment;
import com.example.common.widget.EmptyView;
import com.example.common.widget.PortraitView;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.card.UserCard;
import com.example.factory.persenter.contact.FollowContact;
import com.example.factory.persenter.contact.FollowPresenter;
import com.example.factory.persenter.search.SearchContract;
import com.example.factory.persenter.search.SearchUserPresenter;
import com.example.im2.R;
import com.example.im2.activites.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter> implements SearchActivity.SearchFragment,SearchContract.UserView {

    @BindView(R.id.empty)
    EmptyView emptyView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private RecyclerAdapter<UserCard> adapter;



    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new RecyclerAdapter<UserCard>() {

            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
        emptyView.bind(recyclerView);
        setmPlaceHolderView(emptyView);

    }

    @Override
    protected void initData() {
        super.initData();
        search("");
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        adapter.replace(userCards);
        mPlaceHolderView.triggerOkOrEmpty(adapter.getItemCount()>0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
    implements FollowContact.View {
        @BindView(R.id.portrait)
        PortraitView portrait;

        @BindView(R.id.add)
        ImageView add;

        @BindView(R.id.name)
        TextView name;

        private FollowContact.Presenter mPresenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 当前
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            portrait.setup(Glide.with(getContext()), userCard);

            name.setText(userCard.getName());
            add.setEnabled(!userCard.isFollow());
        }
        @OnClick(R.id.add)
        void onFollowClick(){
            // 发起关注
            mPresenter.follow(mData.getId());
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {

            if (add.getDrawable() instanceof LoadingDrawable){

                ((LoadingDrawable) add.getDrawable()).stop();
                add.setImageResource(R.drawable.sel_opt_done_add);
            }
            updateData(userCard);
            //mPresenter.follow(mData.getId());
        }

        @Override
        public void showError(int Str) {
            if (add.getDrawable() instanceof LoadingDrawable){
                // 失败就停止
                LoadingDrawable drawable = (LoadingDrawable) add.getDrawable();
                drawable.setProgress(1);
                drawable.stop();

            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 22);

            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);

            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)};

            drawable.setForegroundColor(color);
            add.setImageDrawable(drawable);
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContact.Presenter presenter) {
            mPresenter = presenter;
        }
    }
}
