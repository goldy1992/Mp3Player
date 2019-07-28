package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.R;

public class LinearLayoutWithImageView extends LinearLayout {

    private ImageView view;
    private LayoutInflater inflater;
    Handler mainUpdater;
    private int scale;

    public LinearLayoutWithImageView(Context context) {
        this(context, null);
    }

    public LinearLayoutWithImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutWithImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0, 0, 1);
    }

    public LinearLayoutWithImageView(Context context, AttributeSet attrs,
                                     int defStyleAttr, int defStyleRes, int resource, int scale) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mainUpdater = new Handler(Looper.getMainLooper());
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_linear_layout_with_image_view, this);
        this.view = this.findViewById(R.id.view);
        this.view.setImageResource(resource);
        this.scale = scale;
        init();
    }

    private void init() {
        ImageView imageView = getView();
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
        Drawable drawable = imageView.getDrawable();
        if (null != drawable) {
            int imageHeight = imageView.getDrawable().getIntrinsicHeight();
            long exactMarginSize = imageHeight / 2;
            int marginSize = (int) exactMarginSize;
            imageView.setPadding(marginSize, marginSize, marginSize, marginSize);
        }
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public ImageView getView() {
        return view;
    }

    public void setViewImage(@DrawableRes int resource) {
        Drawable drawable = getContext().getDrawable(resource);
        getView().setImageDrawable(drawable);
    }

    public static LinearLayoutWithImageView create(Context context, int imageResource, OnClickListener onClickListener) {
        LinearLayoutWithImageView toReturn = new LinearLayoutWithImageView(context);
        toReturn.setViewImage(imageResource);
        toReturn.init();
        toReturn.getView().setOnClickListener(onClickListener);
        return toReturn;
    }
}
