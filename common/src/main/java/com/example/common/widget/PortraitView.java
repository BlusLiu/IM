package com.example.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.example.common.R;
import com.example.factory.model.Author;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liuzhen on 2019/10/10
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager, Author author){
        if (author == null)
            return;

        setup(manager, author.getPortrait());
    }

    public void setup(RequestManager manager,String url){
        setup(manager, R.drawable.default_portrait, url);
    }

    public void setup(RequestManager manager, int resourceId,String url){
        if (url == null)
            url = "";
        manager.load(url)
        .placeholder(resourceId)
        .centerCrop()
        .dontAnimate()
        .into(this);
    }
}
