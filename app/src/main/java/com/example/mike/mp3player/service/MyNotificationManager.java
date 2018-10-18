package com.example.mike.mp3player.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaPlayerActivity;

public class MyNotificationManager {

    private final MediaPlaybackService service;

    private final NotificationCompat.Action playAction;
    private final NotificationCompat.Action pauseAction;
    private final NotificationManager notificationManager;

    public static final int NOTIFICATION_ID = 412;
    private static final String TAG = "MY_NOTIFICATION_MANAGER";
    private static final String CHANNEL_ID = "com.example.mike.mp3player.service";
    private static final int REQUEST_CODE = 501;

    public MyNotificationManager(MediaPlaybackService service) {
        this.service = service;
        notificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);

        playAction = new NotificationCompat.Action(
                0, service.getString(R.string.PLAY),
                MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PLAY));
        pauseAction = new NotificationCompat.Action(0, service.getString(R.string.PAUSE),
                    MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PAUSE));

        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        notificationManager.cancelAll();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public Notification getNotification(MediaMetadataCompat metadata,
                                        @NonNull PlaybackStateCompat state,
                                        MediaSessionCompat.Token token) {
        boolean isPlaying = state.getState() == PlaybackStateCompat.STATE_PLAYING;
        MediaDescriptionCompat description = metadata.getDescription();
        NotificationCompat.Builder builder =
                buildNotification(state, token, isPlaying, description);
        return builder.build();
    }

    private NotificationCompat.Builder buildNotification(@NonNull PlaybackStateCompat state,
                                                         MediaSessionCompat.Token token,
                                                         boolean isPlaying,
                                                         MediaDescriptionCompat description) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(service.getApplicationContext());
        builder.setStyle(
                new MediaStyle()
                        .setMediaSession(token)
                        .setShowActionsInCompactView(0, 1, 2)
                        // For backwards compatibility with Android L and earlier.
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(
                                MediaButtonReceiver.buildMediaButtonPendingIntent(
                                        service,
                                        PlaybackStateCompat.ACTION_STOP)))
                .setColor(ContextCompat.getColor(service, R.color.notification_bg))
                .setSmallIcon(0)
                // Pending intent that is fired when user clicks on notification.
                .setContentIntent(createContentIntent())
                // Title - Usually Song name.
                .setContentTitle(description.getTitle())
                // Subtitle - Usually Artist name.
                .setContentText(description.getSubtitle())
             //   .setLargeIcon(MusicLibrary.getAlbumBitmap(service, description.getMediaId()))
                // When notification is deleted (when playback is paused and notification can be
                // deleted) fire MediaButtonPendingIntent with ACTION_STOP.
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(
                        service, PlaybackStateCompat.ACTION_STOP))
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

//        // If skip to next action is enabled.
//        if ((state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
//            builder.addAction(mPrevAction);
//        }

        builder.addAction(isPlaying ? pauseAction : playAction);

        // If skip to prev action is enabled.
//        if ((state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
//            builder.addAction(mNextAction);
//        }

        return builder;
    }


    private PendingIntent createContentIntent() {
        Intent openUI = new Intent(service, MediaPlayerActivity.class);
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(
                service, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT);
    }

}
