package com.example.im2.frags.main;


import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.example.common.widget.GalleyView;
import com.example.im2.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends com.example.common.APP.Fragment {
    @BindView(R.id.galleyView)
    GalleyView mGalley;

    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        Toast.makeText(this.getContext(),"初始化active",Toast.LENGTH_SHORT);
        mGalley.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
