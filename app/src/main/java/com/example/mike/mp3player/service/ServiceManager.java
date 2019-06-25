package com.example.mike.mp3player.service;

import android.app.Notification;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.example.mike.mp3player.service.session.MediaSessionAdapter;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.NO_ACTION;

public class ServiceManager {

    private MediaPlaybackService service;
    private MediaSessionAdapter mediaSession;
    private MyNotificationManager notificationManager;
    private boolean serviceStarted = false;

    @Inject
    public ServiceManager(MediaSessionAdapter mediaSession,
                          MyNotificationManager myNotificationManager) {
        this.mediaSession = mediaSession;
        this.notificationManager = myNotificationManager;
    }

    public void init(MediaPlaybackService mediaPlaybackService) {
        this.service = mediaPlaybackService;
    }

    public void startService(Notification notification) {
        createServiceIfNotStarted();
        mediaSession.setActive(true);
        notificationManager.getNotificationManager().notify(MyNotificationManager.NOTIFICATION_ID, notification);
    }

    public void startService() {
        startService(prepareNotification());
    }

    private void createServiceIfNotStarted() {
        if (!serviceStarted) {
            Intent startServiceIntent = new Intent(service, MediaPlaybackService.class);
            startServiceIntent.setAction("com.example.mike.mp3player.service.MediaPlaybackService");
            ContextCompat.startForegroundService(service, startServiceIntent);
            service.startService(startServiceIntent);
            serviceStarted = true;
        }
    }

    public void stopService() {
        service.stopForeground(true);
        service.stopSelf();
        mediaSession.setActive(false);
        serviceStarted = false;
    }

    public void startMediaSession() {
        mediaSession.setActive(true);
    }

    public void stopMediaSession() {
        mediaSession.setActive(false);
    }

    public void pauseService(Notification notification){
        createServiceIfNotStarted();
        service.stopForeground(false);
        service.stopSelf();
        notifyService(notification);
        serviceStarted = false;
    }

    public void pauseService() {
        pauseService(prepareNotification());
    }
    private Notification prepareNotification() {
        return notificationManager.getNotification(mediaSession.getCurrentMetaData(),
                mediaSession.getCurrentPlaybackState(NO_ACTION));
    }

    public void notifyService(Notification notification) {
        notificationManager.getNotificationManager().notify(MyNotificationManager.NOTIFICATION_ID, notification);
    }

    public void notifyService() {
        notificationManager.getNotificationManager().notify(MyNotificationManager.NOTIFICATION_ID, prepareNotification());
    }

    public MediaPlaybackService getService() {
        return service;
    }

    @Inject
    public void setMediaPlaybackService(MediaPlaybackService mediaPlaybackService) {
        this.service = mediaPlaybackService;
    }
}
