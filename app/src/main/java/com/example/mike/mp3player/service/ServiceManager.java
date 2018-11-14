package com.example.mike.mp3player.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

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
        if (!serviceStarted) {
            Intent startServiceIntent = new Intent(context, MediaPlaybackService.class);
            startServiceIntent.setAction("com.example.mike.mp3player.service.MediaPlaybackService");
            service.startService(startServiceIntent);
            mediaSession.setActive(true);
            serviceStarted = true;
        }

        service.startForeground(MyNotificationManager.NOTIFICATION_ID, notification);
        notificationManager.getNotificationManager().notify(MyNotificationManager.NOTIFICATION_ID, notification);
        //mediaSession.setPlaybackState();
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

    public void pauseService(Notification notification){
        service.stopForeground(false);
        service.stopSelf();
        notificationManager.getNotificationManager().notify(MyNotificationManager.NOTIFICATION_ID, notification);
        serviceStarted = false;
    }

    public MediaPlaybackService getService() {
        return service;
    }
}
