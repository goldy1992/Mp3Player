package com.example.mike.mp3player.client.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.widget.AppCompatSeekBar;

import com.example.mike.mp3player.client.MySeekerMediaControllerCallback;
import com.example.mike.mp3player.client.SeekerBarChangerListener;

public class SeekerBar extends AppCompatSeekBar {

    private MediaControllerCompat mMediaController;
    private MySeekerMediaControllerCallback mControllerCallback;
    private ValueAnimator valueAnimator;
    private boolean isTracking = false;

    public SeekerBar(Context context) {
        super(context);
        super.setOnSeekBarChangeListener(new SeekerBarChangerListener());

    }

    @Override
    public final void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        // Prohibit adding seek listeners to this subclass.
        throw new UnsupportedOperationException("Cannot add listeners to a MediaSeekBar");
    }

    public void setMediaController(final MediaControllerCompat mediaController) {
        if (mediaController != null) {
            mControllerCallback = new MySeekerMediaControllerCallback(this);
            mediaController.registerCallback(mControllerCallback);
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
}
