package com.example.im2.frags.user;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.common.APP.Applocation;
import com.example.common.APP.PresenterFragment;
import com.example.common.widget.PortraitView;
import com.example.factory.persenter.user.UpdateInfoContract;
import com.example.factory.persenter.user.UpdateInfoPresenter;
import com.example.im2.R;
import com.example.im2.activites.MainActivity;
import com.example.im2.frags.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment
        extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View{

    @BindView(R.id.my_potrait)
    PortraitView mPortrait;

    @BindView(R.id.im_sex)
    ImageView mSex;

    @BindView(R.id.edit_desc)
    EditText mDesc;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit_update)
    Button mSubmit;

    private String mPortaitPath;
    private boolean isMan = true;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.btn_submit_update)
    void onSubmitClick(){
        String desc = mDesc.getText().toString();

        mPresenter.update(mPortaitPath, desc, isMan);
    }

    @OnClick(R.id.im_sex)
    void onSexClick(){
        isMan = !isMan;

        Drawable drawable = getResources().getDrawable(isMan?
                R.drawable.ic_sex_man: R.drawable.ic_sex_woman);
        mSex.setImageDrawable(drawable);
        mSex.getBackground().setLevel(isMan? 0:1);
    }

    @OnClick(R.id.my_potrait)
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
            Applocation.showToast(R.string.data_rsp_error_unknown);
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void loadPortrait(Uri uri) {

        mPortaitPath = uri.getPath();

        Glide.with(this.getContext())
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

        //上传头像
/*
        String localPath = uri.getPath();

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                UploadHelper.uploadPortrait(localPath);
            }
        });
*/

    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }

    @Override
    public void updateSucceed() {
         MainActivity.show(getContext());
        Applocation.showToast("修改成功");
         getActivity().finish();
    }

    @Override
    public void showError(int Str) {
        super.showError(Str);
        // 当提示需要显示错误的时候

        // 停止Loading
        mLoading.stop();
        mSex.setEnabled(true);
        mDesc.setEnabled(true);
        mPortrait.setEnabled(true);
        mSubmit.setEnabled(true);

    }

    @Override
    public void showLoading() {
        super.showLoading();

        mLoading.start();
        mSex.setEnabled(false);
        mDesc.setEnabled(false);
        mPortrait.setEnabled(true);
        mSubmit.setEnabled(false);
    }
}
