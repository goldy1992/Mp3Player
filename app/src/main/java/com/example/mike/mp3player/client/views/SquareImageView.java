package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.mike.mp3player.R;

public class SquareImageView extends AppCompatImageView {

    public static final int USE_WIDTH = 0;
    public static final int USE_HEIGHT = 1;

    private final int useWidthOrHeight;

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.useWidthOrHeight = attrs.getAttributeResourceValue(R.attr.useWidthOrHeight, USE_WIDTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        switch (useWidthOrHeight) {
            case USE_WIDTH:
                setDimensions(getMeasuredWidth());
                break;
            case USE_HEIGHT:
                setDimensions(getMeasuredHeight());
                break;
            default: setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @VisibleForTesting
    public void setDimensions(int length) {
        setMeasuredDimension(length, length);
    }
}
