package com.github.goldy1992.mp3player;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.github.goldy1992.mp3player.service.MediaPlaybackServiceInjector;

public class MikesMp3Player extends Application {
    /**
     * Declared in case need in the future
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), MediaPlaybackServiceInjector.class));
        } else {
            startService(new Intent(getApplicationContext(), MediaPlaybackServiceInjector.class));
        }
    }

}
