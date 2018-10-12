package com.example.mike.mp3player.service;

import android.content.Context;
import android.content.Intent;

public class ServiceManager {

    private MediaPlaybackService service;
    private Context context;

    public ServiceManager(MediaPlaybackService service, Context context) {
        this.service = service;
        this.context = context;
    }

    public void startService() {
        Intent startServiceIntent = new Intent(context, MediaPlaybackService.class);
        startServiceIntent.setAction("com.example.mike.mp3player.service.MediaPlaybackService");
        service.startService(startServiceIntent);
    }

    public void stopService() {
        service.stopSelf();
        service.stopForeground(false);
    }

    public void pauseService(){
        service.stopForeground(false);
    }
}
