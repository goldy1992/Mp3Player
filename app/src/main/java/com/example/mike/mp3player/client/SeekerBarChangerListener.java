package com.example.mike.mp3player.client;

import android.support.v4.media.session.MediaControllerCompat;
import android.widget.SeekBar;

import com.example.mike.mp3player.client.view.SeekerBar;

public class SeekerBarChangerListener implements SeekBar.OnSeekBarChangeListener {

    private MediaControllerCompat mMediaController;

    public SeekerBarChangerListener(MediaControllerCompat mMediaController){
        this.mMediaController = mMediaController;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        setTracking(seekBar, true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        setTracking(seekBar, false);
        mMediaController.getTransportControls().seekTo(seekBar.getProgress());
    }

    private void setTracking(SeekBar seekBar, boolean tracking) {
        if (seekBar instanceof  SeekerBar) {
            SeekerBar seekerBar = (SeekerBar) seekBar;
            seekerBar.setTracking(tracking);
        }
    }
}
