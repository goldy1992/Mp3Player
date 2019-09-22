package com.example.mike.mp3player.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.session.MediaSessionCompat.Token;

import androidx.annotation.Nullable;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.activities.MediaPlayerActivityInjector;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import static com.example.mike.mp3player.commons.Constants.MEDIA_SESSION;

public class MyDescriptionAdapter implements PlayerNotificationManager.MediaDescriptionAdapter {
    private final PlaybackManager playbackManager;
    private final Context context;
    private final Token token;
    private static final int REQUEST_CODE = 501;

    public MyDescriptionAdapter(Context context,
                                Token token,
                                PlaybackManager playbackManager) {
        this.context = context;
        this.token = token;
        this.playbackManager = playbackManager;
    }

    @Override
    public String getCurrentContentTitle(Player player) {
        return MediaItemUtils.getTitle(getCurrentMediaItem(player));
    }

    @Nullable
    @Override
    public PendingIntent createCurrentContentIntent(Player player) {
        Intent openUI = new Intent(context, MediaPlayerActivityInjector.class);
        openUI.putExtra(MEDIA_SESSION, token);
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(
                context, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Nullable
    @Override
    public String getCurrentContentText(Player player) {
        return null;
    }

    @Nullable
    @Override
    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
//        Icon icon = Icon.createWithResource(context.getApplicationContext(), R.drawable.ic_music_note);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) icon;
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_music_note);
    }

    private final MediaItem getCurrentMediaItem(Player player) {
        int position = player.getCurrentWindowIndex();
        return playbackManager.getItemAtIndex(position);
    }
}
