package com.example.mike.mp3player.service;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.media.MediaBrowserServiceCompat;

import com.example.mike.mp3player.service.library.ContentManager;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Mike on 24/09/2017.
 */
public abstract class MediaPlaybackService extends MediaBrowserServiceCompat {

    private static final String LOG_TAG = "MEDIA_PLAYBACK_SERVICE";
    private ContentManager contentManager;
    private HandlerThread worker;
    private Handler handler;
    private PlayerNotificationManager playerNotificationManager;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;
    private RootAuthenticator rootAuthenticator;
    private NotificationManager nManager;
    abstract void initialiseDependencies();

    @Override
    public void onCreate() {
        super.onCreate();
        nManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        setSessionToken(mediaSession.getSessionToken());
        playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());
    }


    @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid,
                                 Bundle rootHints) {
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        playerNotificationManager.invalidate();
        stopForeground(true);
        mediaSession.release();
        worker.quitSafely();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        mediaSession.release();
        nManager.cancelAll();
        stopSelf();
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
    public void setMediaSessionConnector(MediaSessionConnector mediaSessionConnector) {
        this.mediaSessionConnector = mediaSessionConnector;
    }

    @Inject
    public void setPlayerNotificationManager(PlayerNotificationManager playerNotificationManager) {
        this.playerNotificationManager = playerNotificationManager;
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