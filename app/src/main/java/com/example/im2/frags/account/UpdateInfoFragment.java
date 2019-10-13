package com.example.im2.frags.account;



import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.common.APP.Applocation;
import com.example.common.widget.PotraitView;
import com.example.im2.R;
import com.example.im2.frags.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment extends com.example.common.APP.Fragment {
    @BindView(R.id.im_portrait)
    PotraitView mPortrait;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();

                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

                File dPath = Applocation.getPortaitTmpFile();
                options.setCompressionQuality(96);
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                        .withAspectRatio(1,1)
                        .withMaxResultSize(520,520)
                        .withOptions(options)
                        .start(getActivity());

                //activity收到回调
            }
        })
        //show的时候建议使用getChildFragmentManager
        .show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void loadPortrait(Uri uri) {
        Glide.with(this.getContext())
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);
    }
}
