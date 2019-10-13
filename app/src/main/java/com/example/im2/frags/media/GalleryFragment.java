package com.example.im2.frags.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.common.widget.GalleryView;
import com.example.im2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.qiujuer.genius.ui.Ui;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangeListener {

    private GalleryView mGallery;
    private OnSelectedListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = (GalleryView)root.findViewById(R.id.galleryView);
        // 这里为空指针
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        //这里怎么会有空指针？！！！
        mGallery.setup(getLoaderManager(), this);
        Log.d("Gallery", "onStart: ");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if (count > 0){
            dismiss();

            if(mListener != null){
                String[] paths = mGallery.getSelectPath();

                mListener.onSelectedImage(paths[0]);
                //加快内存回收
                mListener = null;
            }
        }
    }

    /**
     * 设置监听事件，并返回自己
     * @param listener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener){
        mListener = listener;
        return this;
    }

    /**
     * 选择图片监听器
     */
    public interface OnSelectedListener{
        void onSelectedImage(String path);
    }

    private static class  TransStatusBottomSheetDialog extends BottomSheetDialog{

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if (window == null)
                return;

            // height
            int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;

            // 状态栏的高度 TODO 动态获取高度
            int statusHeight = (int)Ui.dipToPx(getContext().getResources(),25);

            // 计算dialog的高度
            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0?ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }
    }
}
