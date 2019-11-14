package com.github.goldy1992.mp3player.dagger.modules;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.service.MyDescriptionAdapter;
import com.github.goldy1992.mp3player.service.PlaylistManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaybackNotificationManagerModule {

    public static final int NOTIFICATION_ID = 512;
    private static final int CHANNEL_D = 704;
    private static final String CHANNEL_ID = "com.github.goldy1992.mp3player.context";

    @Provides
    @Singleton
    public PlayerNotificationManager providesPlayerNotificationManager(
            Context context, MyDescriptionAdapter myDescriptionAdapter, ExoPlayer exoPlayer)
    {
        PlayerNotificationManager playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(context,
                CHANNEL_ID, R.string.notification_channel_name, R.string.channel_description, NOTIFICATION_ID, myDescriptionAdapter);
        playerNotificationManager.setPlayer(exoPlayer);
        playerNotificationManager.setColor(Color.BLACK);
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_LOW);
        playerNotificationManager.setColorized(true);
        playerNotificationManager.setUseChronometer(true);
        playerNotificationManager.setSmallIcon(R.drawable.exo_notification_small_icon);
        playerNotificationManager.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        return playerNotificationManager;
    }

    @Provides
    @Singleton
    public MyDescriptionAdapter providesMyDescriptionAdapter(Context context,
                                                             MediaSessionCompat.Token token,
                                                             PlaylistManager playlistManager) {
        return new MyDescriptionAdapter(context, token, playlistManager);
    }
}
