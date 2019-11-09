package com.github.goldy1992.mp3player;

import android.app.Application;
import android.content.Intent;

import com.github.goldy1992.mp3player.service.KillNotificationService;

public class MikesMp3Player extends Application {
    /**
     * Declared in case need in the future
     */
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, KillNotificationService.class));
    }

}
