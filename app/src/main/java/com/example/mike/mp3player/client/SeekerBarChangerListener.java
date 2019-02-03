package com.example.mike.mp3player.client;

import android.widget.SeekBar;

import com.example.mike.mp3player.client.views.SeekerBar;

public class SeekerBarChangerListener implements SeekBar.OnSeekBarChangeListener {

    private MediaControllerAdapter mediaControllerAdapter;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
        mediaControllerAdapter.seekTo(seekBar.getProgress());
    }

    private void setTracking(SeekBar seekBar, boolean tracking) {
        if (seekBar instanceof  SeekerBar) {
            SeekerBar seekerBar = (SeekerBar) seekBar;
            seekerBar.setTracking(tracking);
        }
    }


    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }
}
