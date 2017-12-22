package com.example.mike.mp3player.service;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Created by Mike on 21/12/2017.
 */
// MediaPlayerAdapter Callback: MediaPlayerAdapter state -> MusicService.
public class MediaPlayerListener extends PlaybackInfoListener {

    private final ServiceManager mServiceManager;
    private final MediaSessionCompat mSession;

    public MediaPlayerListener(MediaSessionCompat mediaSessionCompat) {
        mSession = mediaSessionCompat;
        mServiceManager = new ServiceManager();
    }

    @Override
    public void onPlaybackStateChange(PlaybackStateCompat state) {
        // Report the state to the MediaSession.
        mSession.setPlaybackState(state);

        // Manage the started state of this service.
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                getmServiceManager().moveServiceToStartedState(state);
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                getmServiceManager().updateNotificationForPause(state);
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                getmServiceManager().moveServiceOutOfStartedState(state);
                break;
        }
    }

    public ServiceManager getmServiceManager() {
        return mServiceManager;
    }
} // class
