package com.example.mike.mp3player.client.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.example.mike.mp3player.R;

public class SeekerBar extends AppCompatSeekBar {

    private ValueAnimator valueAnimator;
    private boolean isTracking = false;

    public SeekerBar(Context context) {
        this(context, null);
    }

    public SeekerBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarStyle);
    }

    public SeekerBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setVisibility(VISIBLE);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public final void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        super.setOnSeekBarChangeListener(listener);
    }

    public ValueAnimator getValueAnimator() {
        return valueAnimator;
    }

    public void setValueAnimator(ValueAnimator valueAnimator) {
        this.valueAnimator = valueAnimator;
    }

    public boolean isTracking() {
        return isTracking;
    }

    public void setTracking(boolean tracking) {
        isTracking = tracking;
    }

}
