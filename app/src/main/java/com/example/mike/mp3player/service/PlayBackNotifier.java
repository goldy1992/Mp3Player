package com.example.mike.mp3player.service;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

public class PlayBackNotifier {

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playBackStateBuilder;

    public PlayBackNotifier(MediaSessionCompat mediaSession) {
        this.mediaSession = mediaSession;

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        playBackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_STOP);
        mediaSession.setPlaybackState(playBackStateBuilder.build());
    }

    public void notifyPlay(long position, float playbackSpeed) {
        playBackStateBuilder = new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING, position, playbackSpeed);
        sendPlaybackState(playBackStateBuilder.build());
    }

    public void notifyPause(long position) {
        playBackStateBuilder = new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, position, 0f);
        sendPlaybackState(playBackStateBuilder.build());
    }

    public void notifySeekTo(int state, long position, float playbackSpeed) {
        playBackStateBuilder = new PlaybackStateCompat.Builder().setState(state, position, playbackSpeed);
        sendPlaybackState(playBackStateBuilder.build());
    }

    public void notifyStop() {
        playBackStateBuilder = new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_STOPPED, 0L, 0f);
        mediaSession.setPlaybackState(playBackStateBuilder.build());
    }

    private void sendPlaybackState(PlaybackStateCompat state) {
        mediaSession.setPlaybackState(state);
    }
}
