package com.github.goldy1992.mp3player.client.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.AppCompatImageView;

import com.github.goldy1992.mp3player.R;

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
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView);
            this.useWidthOrHeight = typedArray.getInteger(R.styleable.SquareImageView_useWidthOrHeight, USE_WIDTH);
        } else {
            this.useWidthOrHeight = USE_WIDTH;
        }
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
