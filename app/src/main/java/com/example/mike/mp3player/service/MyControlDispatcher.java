package com.example.mike.mp3player.service;

import com.example.mike.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver;
import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.Player;

public class MyControlDispatcher extends DefaultControlDispatcher {

    private final AudioBecomingNoisyBroadcastReceiver audioBecomingNoisyBroadcastReceiver;

    public MyControlDispatcher(AudioBecomingNoisyBroadcastReceiver audioBecomingNoisyBroadcastReceiver) {
        this.audioBecomingNoisyBroadcastReceiver = audioBecomingNoisyBroadcastReceiver;
    }

    @Override
    public boolean dispatchSetPlayWhenReady(Player player, boolean playWhenReady) {
        if (playWhenReady) {
            audioBecomingNoisyBroadcastReceiver.register();
        } else {
            audioBecomingNoisyBroadcastReceiver.unregister();
        }
       return super.dispatchSetPlayWhenReady(player, playWhenReady);
    }

}
