package com.example.mike.mp3player.service;

import android.app.Notification;
import android.content.Context;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaSessionCallback extends MediaSessionCompat.Callback {

    private ServiceManager serviceManager;
    private MyMediaPlayerAdapter myMediaPlayerAdapter;
    private MediaSessionCompat mediaSession;
    private MyNotificationManager myNotificationManager;

    public MediaSessionCallback(Context context, MyNotificationManager myNotificationManager, ServiceManager serviceManager, MediaSessionCompat mediaSession) {
        this.serviceManager = serviceManager;
        this.mediaSession = mediaSession;
        this.myNotificationManager = myNotificationManager;
        this.myMediaPlayerAdapter = new MyMediaPlayerAdapter(context);
        this.myMediaPlayerAdapter.init();
    }

    @Override
    public void onPlay() {
        myMediaPlayerAdapter.play();
        mediaSession.setPlaybackState(myMediaPlayerAdapter.getMediaPlayerState());
        mediaSession.setMetadata(myMediaPlayerAdapter.getCurrentMetaData());

        serviceManager.startService(prepareNotification());
    }

    @Override
    public void onPrepareFromUri(Uri uri, Bundle bundle) {
        super.onPrepareFromUri(uri, bundle);
        myMediaPlayerAdapter.prepareFromUri(uri);
        mediaSession.setPlaybackState(myMediaPlayerAdapter.getMediaPlayerState());
        mediaSession.setMetadata(myMediaPlayerAdapter.getCurrentMetaData());
    }

    @Override
    public void onPlayFromUri(Uri uri, Bundle bundle) {
        super.onPlayFromUri(uri, bundle);
        myMediaPlayerAdapter.playFromUri(uri);
        serviceManager.startMediaSession();
    }

    @Override
    public void onStop() {
        myMediaPlayerAdapter.stop();
        serviceManager.stopService();
    }

    @Override
    public void onPause() {
        myMediaPlayerAdapter.pause();
        // unregister BECOME_NOISY BroadcastReceiver
//        unregisterReceiver(myNoisyAudioStreamReceiver, intentFilter);
        // Take the serviceManager out of the foreground, retain the notification
        serviceManager.pauseService(prepareNotification());
    }

    @Override
    public void onPrepare() {
//        if (!mediaSession.isActive()) {
//            mediaSession.setActive(true);
//        } // if session active
    } // onPrepare

    @Override
    public void onSeekTo(long position ) {
        myMediaPlayerAdapter.seekTo(position);
    }

    private Notification prepareNotification() {
        return myNotificationManager.getNotification(myMediaPlayerAdapter.getCurrentMetaData(),
                myMediaPlayerAdapter.getMediaPlayerState(),
                mediaSession.getSessionToken());
    }
}