package com.example.mike.mp3player.client.views;

import android.animation.ValueAnimator;
import android.content.Context;
import androidx.appcompat.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.SeekerBarController;

public class SeekerBar extends AppCompatSeekBar {

    private ValueAnimator valueAnimator;
    private boolean isTracking = false;

    private SeekerBarController seekerBarController;
    private TimeCounter timeCounter;

    public SeekerBar(Context context) {
        super(context);
    }

    public SeekerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekerBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(MediaControllerAdapter mediaControllerAdapter) {
        this.seekerBarController = new SeekerBarController(this, mediaControllerAdapter);
        super.setOnSeekBarChangeListener(seekerBarController);
    }

    @Override
    public final void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        super.setOnSeekBarChangeListener(l);
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

    public SeekerBarController getSeekerBarController() {
        return seekerBarController;
    }

    public void setTimeCounter(TimeCounter timeCounter) {
        this.timeCounter = timeCounter;
    }

    public TimeCounter getTimeCounter() {
        return timeCounter;
    }
}
