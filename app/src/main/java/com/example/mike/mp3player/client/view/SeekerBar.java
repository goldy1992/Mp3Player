package com.example.mike.mp3player.client.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import com.example.mike.mp3player.client.callbacks.MySeekerMediaControllerCallback;
import com.example.mike.mp3player.client.SeekerBarChangerListener;

public class SeekerBar extends AppCompatSeekBar {

    private MediaControllerCompat mMediaController;
    private MySeekerMediaControllerCallback mControllerCallback;
    private ValueAnimator valueAnimator;
    private boolean isTracking = false;
    private TimeCounter timeCounter;

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

    @Override
    public final void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        super.setOnSeekBarChangeListener(l);
    }

    public void setMediaController(final MediaControllerCompat mediaController) {
        if (mediaController != null) {
            mControllerCallback = new MySeekerMediaControllerCallback(this);
            SeekerBarChangerListener seekerBarChangerListener = new SeekerBarChangerListener(mediaController);
            this.seekBarChangeListener = seekerBarChangerListener;
            super.setOnSeekBarChangeListener(seekerBarChangerListener);
        } else if (mMediaController != null) {
            mMediaController.unregisterCallback(mControllerCallback);
            mControllerCallback = null;
        }
        mMediaController = mediaController;
    }

    public void disconnectController() {
        if (mMediaController != null) {
            mMediaController.unregisterCallback(mControllerCallback);
            mControllerCallback = null;
            mMediaController = null;
        }
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
}
