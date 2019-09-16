package com.example.mike.mp3player.service.player;

import android.content.Intent;

import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;

public class MyMediaButtonEventHandler implements MediaSessionConnector.MediaButtonEventHandler {

    @Override
    public boolean onMediaButtonEvent(Player player, ControlDispatcher controlDispatcher, Intent mediaButtonEvent) {
        return false;
    }
}
