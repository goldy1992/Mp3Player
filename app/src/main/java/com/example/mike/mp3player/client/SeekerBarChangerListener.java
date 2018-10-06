package com.example.mike.mp3player.client;

import android.widget.SeekBar;

import com.example.mike.mp3player.client.view.SeekerBar;

public class SeekerBarChangerListener implements SeekBar.OnSeekBarChangeListener {


    public SeekerBarChangerListener(){
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar instanceof  SeekerBar)
        {
            SeekerBar seekerBar = (SeekerBar) seekBar;
            seekerBar.setTracking(true);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
