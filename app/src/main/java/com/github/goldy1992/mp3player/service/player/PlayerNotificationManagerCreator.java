package com.github.goldy1992.mp3player.service.player;

import android.content.Context;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.service.MyDescriptionAdapter;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayerNotificationManagerCreator {

    private static final String LOG_TAG = "MEDIA_PLAYBACK_SERVICE";
    private static final int NOTIFICATION_ID = 512;
    private static final int CHANNEL_D = 704;

    private static final String CHANNEL_ID = "com.github.goldy1992.mp3player.context";

    private PlayerNotificationManager playbackNotificationManager;
    
    private ExoPlayer exoPlayer;
    private Context context;
    private MyDescriptionAdapter myDescriptionAdapter;

    @Inject
    public PlayerNotificationManagerCreator(Context context, MyDescriptionAdapter myDescriptionAdapter, ExoPlayer exoPlayer) {
        this.context = context;
        this.myDescriptionAdapter = myDescriptionAdapter;
        this.exoPlayer = exoPlayer;


    }

    public PlayerNotificationManager create() {
        if (null == playbackNotificationManager) {
            this.playbackNotificationManager = PlayerNotificationManager.createWithNotificationChannel(context,
                    CHANNEL_ID, R.string.notification_channel_name, R.string.channel_description, NOTIFICATION_ID, myDescriptionAdapter);

            this.playbackNotificationManager.setPlayer(exoPlayer);
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
}
