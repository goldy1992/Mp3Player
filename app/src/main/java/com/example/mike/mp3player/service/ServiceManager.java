package com.example.mike.mp3player.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.example.mike.mp3player.service.session.MediaSessionAdapter;

import static com.example.mike.mp3player.commons.Constants.NO_ACTION;

public class ServiceManager {

    private MediaPlaybackService service;
    private Context context;
    private MediaSessionAdapter mediaSession;
    private MyNotificationManager notificationManager;
    private boolean serviceStarted = false;

    public ServiceManager(MediaPlaybackService service,
                          MediaSessionAdapter mediaSession) {
        this.service = service;
        this.context = service.getApplicationContext();
        this.mediaSession = mediaSession;
        this.notificationManager = new MyNotificationManager(context, mediaSession.getMediaSessionToken());
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
            Intent startServiceIntent = new Intent(context, MediaPlaybackService.class);
            startServiceIntent.setAction("com.example.mike.mp3player.service.MediaPlaybackService");
            ContextCompat.startForegroundService(context, startServiceIntent);
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
}
