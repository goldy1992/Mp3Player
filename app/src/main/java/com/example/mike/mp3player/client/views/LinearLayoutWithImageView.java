package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mike.mp3player.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public class LinearLayoutWithImageView extends LinearLayout {

    private ImageView view;
    private LayoutInflater inflater;
    Handler mainUpdater;

    public LinearLayoutWithImageView(Context context) {
        this(context, null);
    }

    public LinearLayoutWithImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutWithImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LinearLayoutWithImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mainUpdater = new Handler(Looper.getMainLooper());
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_linear_layout_with_image_view, this);
        this.view = this.findViewById(R.id.view);
        //this.view.setImageDrawable(getDrawableResourceFromAttribute(attrs));
    }
    private Drawable getDrawableResourceFromAttribute(AttributeSet attrs) {
        int resourceToReturn = 0;
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.LinearLayoutWithImageView,
                    0, 0);

            try {
                resourceToReturn = a.getResourceId(R.styleable.LinearLayoutWithImageView_imgSrc, 0);
            } finally {
                a.recycle();
            }
        }
        return getContext().getResources().getDrawable(resourceToReturn, null);
    }

    public void init() {
        ImageView imageView = getView();
        imageView.setScaleX(2);
        imageView.setScaleY(2);
        int imageHeight = imageView.getDrawable().getIntrinsicHeight();
        long exactMarginSize =  imageHeight / 2;
        int marginSize =  (int) exactMarginSize;
        imageView.setPadding(marginSize, marginSize, marginSize, marginSize);
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
