package com.example.im2.frags.main;


import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.common.APP.PresenterFragment;
import com.example.common.widget.EmptyView;
import com.example.common.widget.GalleryView;
import com.example.common.widget.PortraitView;
import com.example.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.Session;
import com.example.factory.model.db.User;
import com.example.factory.persenter.message.ChatContract;
import com.example.factory.persenter.message.SessionContact;
import com.example.factory.persenter.message.SessionPresenter;
import com.example.im2.R;
import com.example.im2.activites.MessageActivity;
import com.example.im2.activites.PersonalActivity;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends PresenterFragment<SessionContact.Presenter> implements SessionContact.View  {
    @BindView(R.id.empty)
    EmptyView emptyView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private RecyclerAdapter<Session> mAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new RecyclerAdapter<Session>() {

            @Override
            protected int getItemViewType(int position, Session session) {
                return R.layout.cell_session_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {

                return new ActiveFragment.ViewHolder(root);
            }
        });
        mAdapter.setmListener(new RecyclerAdapter.AdapterListener<Session>() {
            @Override
            public void onItenClick(RecyclerAdapter.ViewHolder holder, Session session) {
                User user = new User();
                user.setId(session.getId());
                user.setPortrait(session.getPicture());
                user.setName(session.getTitle());
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
    protected SessionContact.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChange() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);

    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Session>{
        @BindView(R.id.im_portrait)
        PortraitView portrait;

        @BindView(R.id.txt_content)
        TextView content;

        @BindView(R.id.txt_name)
        TextView name;

        @BindView(R.id.txt_time)
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {
            portrait.setup(Glide.with(getContext()), session.getPicture());
            name.setText(session.getTitle());
            content.setText(session.getContent());
            //new SimpleDateFormat().format(session.getModifyAt());
            time.setText(new SimpleDateFormat().format(session.getModifyAt()));
        }


    }
//    @Override
//    protected void initData() {
//        super.initData();
//
//        if (ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
//        {
//                /*requestPermissions(new String[]
//                                {Manifest.permission.WRITE_CONTACTS},
//                        REQUEST_CODE_ASK_PERMISSIONS);*/
//
//
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//        }
//        mGalley.setmActivity(getActivity());//动态获取activity地址
//        mGalley.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
//            @Override
//            public void onSelectedCountChanged(int count) {
//
//            }
//        });
//    }
}
