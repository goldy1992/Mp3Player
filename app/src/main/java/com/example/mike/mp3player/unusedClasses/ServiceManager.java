package com.example.mike.mp3player.unusedClasses;

import android.app.Notification;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.MediaSessionCallback;

/**
 * Created by Mike on 21/12/2017.
 */

public class ServiceManager {
    private MediaNotificationManager mMediaNotificationManager;
    private MediaPlayerAdapter mPlayback;
    private MediaSessionCompat mSession;
    private MediaSessionCallback mediaSessionCallback;
    private MediaPlaybackService service;
    private boolean mServiceInStartedState;

    public ServiceManager() {}

    public void initServiceManager(MediaSessionCallback mediaSessionCallback) {
        this.mediaSessionCallback = mediaSessionCallback;
        this.mMediaNotificationManager = mediaSessionCallback.getmMediaNotificationManager();
        this.mPlayback = mediaSessionCallback.getMediaPlayerAdapter();
        this.mSession = mediaSessionCallback.getMediaSession();
        this.service = mediaSessionCallback.getService();
    }

    public void moveServiceToStartedState(PlaybackStateCompat state) {
        Notification notification =
                mMediaNotificationManager.getNotification(
                        mPlayback.getCurrentMedia(), state, mSession.getSessionToken());

        if (!mServiceInStartedState) {
           // service.startForegroundService(new Intent(service, MediaPlaybackService.class));
            mServiceInStartedState = true;
        }

        service.startForeground(MediaNotificationManager.NOTIFICATION_ID, notification);
    }

    public void updateNotificationForPause(PlaybackStateCompat state) {
        service.stopForeground(false);
        Notification notification =
                mMediaNotificationManager.getNotification(
                        mPlayback.getCurrentMedia(), state, mSession.getSessionToken());
        mMediaNotificationManager.getNotificationManager()
                .notify(MediaNotificationManager.NOTIFICATION_ID, notification);
    }

    public void moveServiceOutOfStartedState(PlaybackStateCompat state) {
        MediaPlaybackService service = mediaSessionCallback.getService();
        service.stopForeground(true);
        service.stopSelf();
        mServiceInStartedState = false;
    }

}
