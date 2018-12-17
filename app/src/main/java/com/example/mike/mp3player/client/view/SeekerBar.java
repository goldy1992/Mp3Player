package com.example.mike.mp3player.client.view;

import android.animation.ValueAnimator;
import android.content.Context;
import androidx.appcompat.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import com.example.mike.mp3player.client.MediaActivityCompat;
import com.example.mike.mp3player.client.SeekerBarChangerListener;
import com.example.mike.mp3player.client.callbacks.MySeekerMediaControllerCallback;

public class SeekerBar extends AppCompatSeekBar {

    private ValueAnimator valueAnimator;
    private MediaActivityCompat parentActivity;
    private boolean isTracking = false;
    private TimeCounter timeCounter;

    private MySeekerMediaControllerCallback mySeekerMediaControllerCallback;
    public OnSeekBarChangeListener seekBarChangeListener;

    public SeekerBar(Context context) {
        super(context);
    }

    public SeekerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekerBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
        this.mySeekerMediaControllerCallback = new MySeekerMediaControllerCallback(this);
        this.seekBarChangeListener = new SeekerBarChangerListener();
        super.setOnSeekBarChangeListener(seekBarChangeListener);
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

    public OnSeekBarChangeListener getOnSeekBarChangeListener(){
        return seekBarChangeListener;
    }

    public void setTimeCounter(TimeCounter timeCounter) {
        this.timeCounter = timeCounter;
    }

    public TimeCounter getTimeCounter() {
        return timeCounter;
    }

    public MediaActivityCompat getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(MediaActivityCompat parentActivity) {
        this.parentActivity = parentActivity;
    }

    public MySeekerMediaControllerCallback getMySeekerMediaControllerCallback() {
        return mySeekerMediaControllerCallback;
    }
}
