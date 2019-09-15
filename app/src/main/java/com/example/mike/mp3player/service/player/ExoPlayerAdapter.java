package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.mike.mp3player.service.AudioFocusManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSpec;

public class ExoPlayerAdapter implements Player.EventListener {

    ExoPlayer exoPlayer;

    TrackSelector trackSelector;

    public ExoPlayerAdapter(Context context, AudioFocusManager audioFocusManager) {
        MediaSessionConnector mediaSessionConnector;

        com.google.android.exoplayer2.source.ProgressiveMediaSource
        // DataSpec dataSpec =

       // this.trackSelector = new DefaultTrackSelector();
        this.exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
       // this.exoPlayer.

    }



}
