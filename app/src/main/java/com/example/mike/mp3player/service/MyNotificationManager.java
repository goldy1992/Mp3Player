package com.example.mike.mp3player.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
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
    private final NotificationCompat.Action skipToNextAction;
    private final NotificationCompat.Action skipToPreviousAction;

    private final NotificationManager notificationManager;

    public static final int NOTIFICATION_ID = 512;
    private static final String TAG = "MY_NOTIFICATION_MANAGER";
    private static final String CHANNEL_ID = "com.example.mike.mp3player.service";
    private static final int REQUEST_CODE = 501;

    public MyNotificationManager(MediaPlaybackService service) {
        this.service = service;
        notificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);

        playAction = makeAction(android.R.drawable.ic_media_play, service.getString(R.string.PLAY), PlaybackStateCompat.ACTION_PLAY);
        pauseAction = makeAction(android.R.drawable.ic_media_pause, service.getString(R.string.PAUSE), PlaybackStateCompat.ACTION_PAUSE);
        skipToNextAction = makeAction(android.R.drawable.ic_media_next, service.getString(R.string.SKIP_TO_NEXT), PlaybackStateCompat.ACTION_SKIP_TO_NEXT);
        skipToPreviousAction = makeAction(android.R.drawable.ic_media_previous, service.getString(R.string.SKIP_TO_PREVIOUS), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

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

    public NotificationCompat.Builder getNotification(MediaMetadataCompat metadata,
                                                      @NonNull PlaybackStateCompat state,
                                                      MediaSessionCompat.Token token) {
        boolean isPlaying = state.getState() == PlaybackStateCompat.STATE_PLAYING;
        MediaDescriptionCompat description = metadata.getDescription();
        NotificationCompat.Builder builder =
                buildNotification(state, token, isPlaying, description);
        return builder;
    }

    private NotificationCompat.Builder buildNotification(@NonNull PlaybackStateCompat state,
                                                         MediaSessionCompat.Token token,
                                                         boolean isPlaying,
                                                         MediaDescriptionCompat description) {

        // Create the (mandatory) notification channel when running on Android Oreo.
        if (isAndroidOreoOrHigher()) {
            createChannel();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service.getApplicationContext(), CHANNEL_ID);

        builder.addAction(skipToPreviousAction);
        NotificationCompat.Action playPauseAction = isPlaying ? pauseAction : playAction;
        builder.addAction(playPauseAction);
        builder.addAction(skipToNextAction);
        MediaStyle mediaStyle = new MediaStyle();
        NotificationCompat.Style style = mediaStyle.setMediaSession(token)
                .setShowCancelButton(true)

                .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(
                        service,
                        PlaybackStateCompat.ACTION_STOP))
                .setShowActionsInCompactView(0,1,2);

        builder
                .setStyle(style)
                .setColor(ContextCompat.getColor(service, R.color.colorPrimary))
                .setSmallIcon(android.R.drawable.btn_default_small)
                // Pending intent that is fired when user clicks on notification.
                .setContentIntent(createContentIntent())
                // Title - Usually Song name.
                .setContentTitle(description.getTitle())
                // Subtitle - Usually Artist name.
                .setContentText(description.getSubtitle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
          //     .setLargeIcon(android.R)
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


        // If skip to prev action is enabled.
//        if ((state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
//            builder.addAction(mNextAction);
//        }

        return builder;
    }

    // Does nothing on versions of Android earlier than Oreo.
    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            CharSequence name = "MediaSession";
            // The user-visible description of the channel.
            String description = "MediaSession and MediaPlayer";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(
                    new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            notificationManager.createNotificationChannel(mChannel);
            Log.d(TAG, "createChannel: New channel created");
        } else {
            Log.d(TAG, "createChannel: Existing channel reused");
        }
    }

    private boolean isAndroidOreoOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private PendingIntent createContentIntent() {
        Intent openUI = new Intent(service, MediaPlayerActivity.class);
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(
                service, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private NotificationCompat.Action makeAction(int id, String title, long action) {
        PendingIntent pauseIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(service, action);
        return new NotificationCompat.Action(id, title, pauseIntent);
    }

}
