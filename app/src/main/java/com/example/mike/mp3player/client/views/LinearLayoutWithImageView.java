package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class LinearLayoutWithImageView extends LinearLayout {

    private ImageView imageView;
    private LayoutInflater inflater;
    int resource;
    private int scale;

    public LinearLayoutWithImageView(Context context) {
        this(context, null);
    }

    public LinearLayoutWithImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public LinearLayoutWithImageView(Context context, AttributeSet attrs,
                                     int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_linear_layout_with_image_view, this);
        this.imageView = this.findViewById(R.id.image_view);
    }

    public void init(@DrawableRes int resource, int scale) {
        this.setBackgroundColor(Color.CYAN);
        this.resource = resource;
        this.scale = scale;
        this.imageView.setImageResource(resource);
        this.imageView.setScaleX(scale);
        this.imageView.setScaleY(scale);
//        Drawable drawable = imageView.getDrawable();
//        if (null != drawable) {
//            int imageHeight = imageView.getDrawable().getIntrinsicHeight();
//            long exactMarginSize = imageHeight / 2;
//            int marginSize = (int) exactMarginSize;
//            imageView.setPadding(marginSize, marginSize, marginSize, marginSize);
//        }
        setGravity(Gravity.CENTER_HORIZONTAL);
    }



    public void setViewImage(@DrawableRes int resource) {
        Drawable drawable = getContext().getDrawable(resource);
        imageView.setImageDrawable(drawable);
    }

    /**
     * sets alpha level of the ImageView
     * @param alpha the alpha level
     */
    public void setImageAlpha(int alpha) {
        imageView.setImageAlpha(alpha);
    }

    public LinearLayout.LayoutParams getLinearLayoutParams(ViewGroup.LayoutParams params) {
        if (params != null) {
            return new LinearLayout.LayoutParams(params.width,
                    params.height, 1.0f);
        }
        return new LinearLayout.LayoutParams(WRAP_CONTENT,
                WRAP_CONTENT, 1.0f);
    }
}
