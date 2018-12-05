package com.example.mike.mp3player.client;

import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.SeekBar;

import com.example.mike.mp3player.client.view.SeekerBar;
import com.example.mike.mp3player.client.view.TimeCounter;

public class SeekerBarChangerListener implements SeekBar.OnSeekBarChangeListener {

    private TimeCounter timeCounter;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SeekerBar seekerBar = (SeekerBar) seekBar;
        if (seekerBar.isTracking())
        {
            updateTimeCounter(seekerBar);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        SeekerBar seekerBar = (SeekerBar) seekBar;
        seekerBar.getTimeCounter().cancelTimerDuringTracking();
        setTracking(seekBar, true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        setTracking(seekBar, false);
        SeekerBar seekerBar = (SeekerBar) seekBar;
        updateTimeCounter(seekerBar);
        seekerBar.getParentActivity().getMediaControllerWrapper().seekTo(seekBar.getProgress());
    }

    private void setTracking(SeekBar seekBar, boolean tracking) {
        if (seekBar instanceof  SeekerBar) {
            SeekerBar seekerBar = (SeekerBar) seekBar;
            seekerBar.setTracking(tracking);
        }
    }

    private void updateTimeCounter(SeekerBar seekerBar) {
        TimeCounter timeCounter = seekerBar.getTimeCounter();
        PlaybackStateCompat newState = new PlaybackStateCompat.Builder()
                .setState(timeCounter.getCurrentState(), (long)timeCounter.getCurrentSpeed(), seekerBar.getProgress())
                .build();
        seekerBar.getTimeCounter().updateState(newState);
    }
}
