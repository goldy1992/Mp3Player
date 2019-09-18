package com.example.mike.mp3player.dagger.modules;

import android.content.Context;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.service.MyDescriptionAdapter;
import com.example.mike.mp3player.service.PlaybackManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaybackNotificationManagerModule {

    public static final int NOTIFICATION_ID = 512;
    private static final String CHANNEL_ID = "com.example.mike.mp3player.context";

    @Provides
    @Singleton
    public PlayerNotificationManager providesPlayerNotificationManager(
            Context context, MyDescriptionAdapter myDescriptionAdapter, ExoPlayer exoPlayer)
    {
        PlayerNotificationManager playerNotificationManager =  new PlayerNotificationManager(
                context,
                CHANNEL_ID,
                NOTIFICATION_ID,
                myDescriptionAdapter);
        playerNotificationManager.setPlayer(exoPlayer);
        //playerNotificationManager.setOngoing(false);
        playerNotificationManager.setColor(Color.BLACK);
        playerNotificationManager.setColorized(true);
        playerNotificationManager.setUseChronometer(true);
        playerNotificationManager.setSmallIcon(R.drawable.exo_notification_small_icon);
        playerNotificationManager.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        return playerNotificationManager;
    }

    @Provides
    @Singleton
    public MyDescriptionAdapter providesMyDescriptionAdapter(PlaybackManager playbackManager) {
        return new MyDescriptionAdapter(playbackManager);
    }
}
