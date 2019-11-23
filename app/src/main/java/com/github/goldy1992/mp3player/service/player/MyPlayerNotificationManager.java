package com.github.goldy1992.mp3player.service.player;

import android.content.Context;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.github.goldy1992.mp3player.LogTagger;
import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.service.MyDescriptionAdapter;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MyPlayerNotificationManager implements LogTagger {

    private static final int NOTIFICATION_ID = 512;

    private static final String CHANNEL_ID = "com.github.goldy1992.mp3player.context";

    private PlayerNotificationManager playbackNotificationManager = null;
    
    private final ExoPlayer exoPlayer;
    private final Context context;
    private final MyDescriptionAdapter myDescriptionAdapter;
    private final NotificationListener notificationListener;
    private boolean isActive = false;

    @Inject
    public MyPlayerNotificationManager(Context context, MyDescriptionAdapter myDescriptionAdapter,
                                       ExoPlayer exoPlayer,
                                       NotificationListener notificationListener) {
        this.context = context;
        this.myDescriptionAdapter = myDescriptionAdapter;
        this.exoPlayer = exoPlayer;
        this.notificationListener = notificationListener;
        create();
    }

    public PlayerNotificationManager create() {
        if (null == playbackNotificationManager) {
            this.playbackNotificationManager =
                PlayerNotificationManager.createWithNotificationChannel(
                    context,
                    CHANNEL_ID,
                    R.string.notification_channel_name,
                    R.string.channel_description,
                    NOTIFICATION_ID,
                    myDescriptionAdapter,
                    notificationListener);
            this.playbackNotificationManager.setPlayer(null);
            this.playbackNotificationManager.setColor(Color.BLACK);
            this.playbackNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            this.playbackNotificationManager.setPriority(NotificationCompat.PRIORITY_LOW);
            this.playbackNotificationManager.setColorized(true);
            this.playbackNotificationManager.setUseChronometer(true);
            this.playbackNotificationManager.setSmallIcon(R.drawable.exo_notification_small_icon);
            this.playbackNotificationManager.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
            this.playbackNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        }
        return playbackNotificationManager;
    }

    public void activate() {
        this.playbackNotificationManager.setPlayer(exoPlayer);
        this.isActive = true;
    }

    public void deactivate() {
        this.playbackNotificationManager.setPlayer(null);
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String getLogTag() {
        return "MEDIA_PLAYBACK_SERVICE";
    }
}
