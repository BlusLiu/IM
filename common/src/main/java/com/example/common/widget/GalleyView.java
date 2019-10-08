package com.example.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.R;
import com.example.common.widget.recycler.RecyclerAdapter;

import java.util.Objects;

/**
 * TODO: document your custom view class.
 */
public class GalleyView extends RecyclerView {
    private static final int LODER_ID = 0x0100;
    private Adapter mAdapter;
    private LoaderCallback loaderCallback = new LoaderCallback();

    public GalleyView(Context context) {
        super(context);
        init(null, 0);
    }

    public GalleyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GalleyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
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
        return true;
    }

    /**
     * 引入loader,初始化方法
     * @param loaderManager
     */
    public int setup(LoaderManager loaderManager) {
        loaderManager.initLoader(LODER_ID,null,loaderCallback);
        return LODER_ID;
    }

    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
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
            return null;
        }


    }
}
