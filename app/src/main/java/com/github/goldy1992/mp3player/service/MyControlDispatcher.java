package com.github.goldy1992.mp3player.service;

import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver;
import com.github.goldy1992.mp3player.service.player.MyPlayerNotificationManager;
import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.Player;

import javax.inject.Inject;

public class MyControlDispatcher extends DefaultControlDispatcher {

    private final AudioBecomingNoisyBroadcastReceiver audioBecomingNoisyBroadcastReceiver;

    private final MyPlayerNotificationManager playerNotificationManager;

    @Inject
    public MyControlDispatcher(AudioBecomingNoisyBroadcastReceiver audioBecomingNoisyBroadcastReceiver,
                               MyPlayerNotificationManager playerNotificationManager) {
        this.audioBecomingNoisyBroadcastReceiver = audioBecomingNoisyBroadcastReceiver;
        this.playerNotificationManager = playerNotificationManager;
    }

    @Override
    public boolean dispatchSetPlayWhenReady(Player player, boolean playWhenReady) {
        if (playWhenReady) {
            audioBecomingNoisyBroadcastReceiver.register();
            if (!this.playerNotificationManager.isActive()) {
                this.playerNotificationManager.activate();
            }
        } else {
            audioBecomingNoisyBroadcastReceiver.unregister();
        }
       return super.dispatchSetPlayWhenReady(player, playWhenReady);
    }

}
