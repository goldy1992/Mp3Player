package com.github.goldy1992.mp3player.service;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.media.MediaBrowserServiceCompat;

import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Mike on 24/09/2017.
 */
public abstract class MediaPlaybackService extends MediaBrowserServiceCompat implements NotificationListener {

    private static final String LOG_TAG = "MEDIA_PLAYBACK_SERVICE";
    private ContentManager contentManager;
    private HandlerThread worker;
    private Handler handler;
    private MediaSessionConnectorCreator mediaSessionConnectorCreator;
    private MediaSessionCompat mediaSession;
    private RootAuthenticator rootAuthenticator;

    abstract void initialiseDependencies();

    @Override
    public void onCreate() {
        super.onCreate();
        this.mediaSessionConnectorCreator.create();
        setSessionToken(mediaSession.getSessionToken());
    }

    @Override
    public int onStartCommand (Intent intent,
                                      int flags,
                                      int startId) {

        Log.i(LOG_TAG, "breakpoint, on start command called");
        return START_STICKY;
    }


    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid,
                                 @Nullable Bundle rootHints) {
        return rootAuthenticator.authenticate(clientPackageName, clientUid, rootHints);
    }

    /**
     * onLoadChildren(String, Result, Bundle) :- onLoadChildren should always be called with a LibraryObject item as a bundle option. Searching for
     * a MediaItem's children is now deprecated as it wasted
     * @param parentId the parent ID
     * @param result the result object used by the MediaBrowserServiceCompat
     */
    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        //  Browsing not allowed
        if (rootAuthenticator.rejectRootSubscription(parentId)) {
            result.sendResult(null);
            return;
        }

        result.detach();
        handler.post(() -> {
            // Assume for example that the music catalog is already loaded/cached.
            List<MediaBrowserCompat.MediaItem> mediaItems = contentManager.getChildren(parentId);
            result.sendResult(mediaItems);
        });
    }

    /**
     * Called each time after the notification has been posted.
     *
     * <p>For a service, the {@code ongoing} flag can be used as an indicator as to whether it
     * should be in the foreground.
     *
     * @param notificationId The id of the notification which has been posted.
     * @param notification The {@link Notification}.
     * @param ongoing Whether the notification is ongoing.
     */
    @Override
    public void onNotificationPosted(
            int notificationId, Notification notification, boolean ongoing) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // fix to make notifications removable on versions < oreo.
            if (!ongoing) {
                stopForeground(false);
            } else {
                startForeground(notificationId, notification);
            }
        }
    }

    @Override
    public void onSearch(@NonNull String query, Bundle extras,
                         @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.detach();
        handler.post(() -> {
            // Assume for example that the music catalog is already loaded/cached.
            List<MediaBrowserCompat.MediaItem> mediaItems = contentManager.search(query);
            result.sendResult(mediaItems);
        });
    }

    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    @Inject
    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }

    @Inject
    public void setMediaSession(MediaSessionCompat mediaSession) {
        this.mediaSession = mediaSession;
    }

    @Inject
    public void setWorker(HandlerThread handlerThread) {
        this.worker = handlerThread;
    }

    @Inject
    public void setHandler(@Named("worker") Handler handler) {
        this.handler = handler;
    }

    @Inject
    public void setRootAuthenticator(RootAuthenticator rootAuthenticator) {
        this.rootAuthenticator = rootAuthenticator;
    }

    @Inject
    public void setMediaSessionConnectorCreator(MediaSessionConnectorCreator mediaSessionConnectorCreator) {
        this.mediaSessionConnectorCreator = mediaSessionConnectorCreator;
    }

    @VisibleForTesting
    public HandlerThread getWorker() {
        return worker;
    }

    @VisibleForTesting
    public ContentManager getContentManager() {
        return contentManager;
    }
}