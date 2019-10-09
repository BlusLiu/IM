package com.example.common.widget;

import android.content.Context;

import android.database.Cursor;

import android.os.Bundle;

import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.common.R;
import com.example.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * TODO: document your custom view class.
 */
public class GalleyView extends RecyclerView {
    private static final int LODER_ID = 0x0100;
    private static final int MAX_IMAGE_COUNT = 3;
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024; //最小图片
    private Adapter mAdapter = new Adapter();
    private LoaderCallback loaderCallback = new LoaderCallback();
    private List<Image> mSelectedImage = new LinkedList<>();
    private SelectedChangeListener mListener;



    public GalleyView(Context context) {
        super(context);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setmListener(new RecyclerAdapter.AdapterListenerImpl<GalleyView.Image>() {
            @Override
            public void onItenClick(RecyclerAdapter.ViewHolder holder, GalleyView.Image image) {
                // cell点击操作
                if (onItemSelectClick(image)){
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     * cell点击逻辑
     * @param image
     * @return
     */
    private boolean onItemSelectClick(Image image){
        boolean notifyRefresh;
        if (mSelectedImage.contains(image)){
            mSelectedImage.remove(image);
            image.isSelect = false;

            notifyRefresh = true;
        }else{
            if (MAX_IMAGE_COUNT <= mSelectedImage.size()){
                Toast.makeText(getContext(), "不需要更新", Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            }else {
                mSelectedImage.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }

        if (notifyRefresh){
            notifySelectChanged();
        }

        return true;
    }

    /**
     * 通知
     */
    private void notifySelectChanged(){
        SelectedChangeListener listener = mListener;
        if (listener != null){
            listener.onSelectedCountChanged(mSelectedImage.size());
        }
    }

    /**
     * 引入loader,初始化方法
     * @param loaderManager
     * @return id用于销毁
     */
    public int setup(LoaderManager loaderManager, SelectedChangeListener listener) {
        mListener = listener;
        loaderManager.initLoader(LODER_ID,null,loaderCallback);
        return LODER_ID;
    }

    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
        private final  String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED
        };


        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            if (id == LODER_ID){
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC");
            }

            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            List<Image> images = new ArrayList<>();
            if (data != null){
                int count = data.getCount();
                if (count > 0){
                    data.moveToFirst();

                    int indexid = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    int indexPath = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    int indexDate = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                    do {
                        // 循环读
                        int id = data.getInt(indexid);
                        String path = data.getString(indexPath);
                        long dateTime = data.getLong(indexDate);

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE){
                            continue;
                        }

                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = dateTime;
                        images.add(image);


                    } while (data.moveToNext());
                }
            }
            if (images.size() == 0)
                Toast.makeText(getContext(), "无资源", Toast.LENGTH_LONG);
            Log.d("bug01","COME HERE");
            System.out.println("COMEHERE!!!");
            updateSource(images);

        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            updateSource(null);
        }
    }

    void updateSource(List<Image> images){
        mAdapter.replace(images);

    }

    /**
     * 选中图片地址
     * @return
     */
    public String [] getSelectPath(){
        String[] paths = new String[mSelectedImage.size()];
        int index = 0;
        for (Image image : mSelectedImage){
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * 清空选中
     */
    public void clear(){
        for (Image image : mSelectedImage) {
            // 先重置
            image.isSelect = false;
        }
        mSelectedImage.clear();
        mAdapter.notifyDataSetChanged();
    }

    private static class Image{
        int id;
        String path;
        long date;
        boolean isSelect;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Image image = (Image) o;
            return Objects.equals(path, image.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path);
        }
    }

    private class Adapter extends RecyclerAdapter<GalleyView.Image>{

        @Override
        protected int getItemViewType(int position, GalleyView.Image image) {
            return R.layout.cell_galley;
        }

        @Override
        protected ViewHolder<GalleyView.Image> onCreateViewHolder(View root, int viewType) {
            return new GalleyView.ViewHolder(root);
        }


    }

    private class  ViewHolder extends RecyclerAdapter.ViewHolder<Image>{

        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;
        public ViewHolder(View itemView){
            super(itemView);

            mPic = (ImageView) itemView.findViewById(R.id.im_image);
            mShade = (View) itemView.findViewById(R.id.view_shade);
            mSelected = (CheckBox) itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .placeholder(R.color.green_200)// 默认颜色
                    .into(mPic);

            mShade.setVisibility(image.isSelect? VISIBLE:INVISIBLE);
            mSelected.setChecked(image.isSelect);
        }
    }

    public interface SelectedChangeListener{
        void onSelectedCountChanged(int count);
    }
}
