package com.example.mike.mp3player.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;

public class ServiceManager {

    private MediaPlaybackService service;
    private Context context;
    private MediaSessionCompat mediaSession;

    public ServiceManager(MediaPlaybackService service, Context context, MediaSessionCompat mediaSession) {
        this.service = service;
        this.context = context;
        this.mediaSession = mediaSession;
    }

    public void startService() {
        Intent startServiceIntent = new Intent(context, MediaPlaybackService.class);
        startServiceIntent.setAction("com.example.mike.mp3player.service.MediaPlaybackService");
        service.startService(startServiceIntent);
        mediaSession.setActive(true);
    }

    public void stopService() {
        service.stopSelf();
        service.stopForeground(false);
        mediaSession.setActive(false);
    }

    public void startMediaSession() {
        mediaSession.setActive(true);
    }

    public void pauseService(){
        service.stopForeground(false);
    }

    public MediaPlaybackService getService() {
        return service;
    }
}
