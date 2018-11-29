package com.example.mike.mp3player.client.callbacks;

import android.animation.ValueAnimator;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.view.SeekerBar;


public class MySeekerMediaControllerCallback extends MediaControllerCompat.Callback implements ValueAnimator.AnimatorUpdateListener {

    private final SeekerBar seekerBar;
    private final int NO_PROGRESS = 0;

    public MySeekerMediaControllerCallback(SeekerBar seekerBar) {
        this.seekerBar = seekerBar;
    }
    @Override
    public void onSessionDestroyed() {
        super.onSessionDestroyed();
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        super.onPlaybackStateChanged(state);

        Log.d("PlaybackStatusCompat", "" + state);
        // If there's an ongoing animation, stop it now.
        if (seekerBar.getValueAnimator() != null) {
            seekerBar.getValueAnimator().cancel();
            seekerBar.setValueAnimator(null);
        }

        long latestPosition = TimerUtils.calculateStartTime(state);
        final int progress = state != null ? (int) latestPosition : NO_PROGRESS;
        seekerBar.setProgress(progress);

        // If the media is playing then the seekbar should follow it, and the easiest
        // way to do that is to create a ValueAnimator to update it so the bar reaches
        // the end of the media the same time as playback gets there (or close enough).
        if (state != null && state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            final int timeToEnd = (int) ((seekerBar.getMax() - progress) / state.getPlaybackSpeed());

            seekerBar.setValueAnimator(ValueAnimator.ofInt(progress, seekerBar.getMax())
                    .setDuration(timeToEnd));
            seekerBar.getValueAnimator().setInterpolator(new LinearInterpolator());
            seekerBar.getValueAnimator().addUpdateListener(this);
            seekerBar.getValueAnimator().start();
        }
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        super.onMetadataChanged(metadata);

        final int max = metadata != null
                ? (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
                : 0;
        seekerBar.setProgress(NO_PROGRESS);
        seekerBar.setMax(max);
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
        // If the user is changing the slider, cancel the animation.
        if (seekerBar.isTracking()) {
            valueAnimator.cancel();
            return;
        }

        final int animatedIntValue = (int) valueAnimator.getAnimatedValue();
        seekerBar.setProgress(animatedIntValue);
    }
}