package com.example.mike.mp3player.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.content.ContextCompat;

public class ServiceManager {

    private MediaPlaybackService service;
    private Context context;
    private MediaSessionCompat mediaSession;
    private MyNotificationManager notificationManager;
    private boolean serviceStarted = false;

    public ServiceManager(MediaPlaybackService service,
                          Context context,
                          MediaSessionCompat mediaSession,
                          MyNotificationManager notificationManager) {
        this.service = service;
        this.context = context;
        this.mediaSession = mediaSession;
        this.notificationManager = notificationManager;
    }

    public void startService(Notification notification) {
        createServiceIfNotStarted();
        mediaSession.setActive(true);
        notificationManager.getNotificationManager().notify(MyNotificationManager.NOTIFICATION_ID, notification);
        //mediaSession.setPlaybackState();
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
        notify(notification);
        serviceStarted = false;
    }

    public void notify(Notification notification) {
        notificationManager.getNotificationManager().notify(MyNotificationManager.NOTIFICATION_ID, notification);
    }

    public MediaPlaybackService getService() {
        return service;
    }
}
