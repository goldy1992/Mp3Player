package com.example.mike.mp3player.service;

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
import com.example.mike.mp3player.service.player.MyMediaButtonEventHandler;
import com.example.mike.mp3player.service.player.MyPlaybackPreparer;
import com.example.mike.mp3player.service.player.MyTimelineQueueNavigator;
import com.example.mike.mp3player.service.session.MediaSessionCallback;
import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;

import java.util.List;

import javax.inject.Inject;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP;

/**
 * Created by Mike on 24/09/2017.
 */
public abstract class MediaPlaybackService extends MediaBrowserServiceCompat {

    @MediaSessionConnector.PlaybackActions
    private static final long SUPPORTED_PLAYBACK_ACTIONS = ACTION_STOP | ACTION_PAUSE | ACTION_PLAY |
            ACTION_SET_REPEAT_MODE | ACTION_SET_SHUFFLE_MODE;

    private static final String LOG_TAG = "MEDIA_PLAYBACK_SERVICE";

    private ContentManager contentManager;
    private HandlerThread worker;
    private Handler handler;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;
    private MediaSessionCallback mediaSessionCallback;
    private RootAuthenticator rootAuthenticator;
    abstract void initialiseDependencies();

    @Override
    public void onCreate() {
        super.onCreate();
        handler.post(() -> {
            this.mediaSessionCallback.init();
            ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
            MyPlaybackPreparer myPlaybackPreparer = new MyPlaybackPreparer(getApplicationContext(), exoPlayer, contentManager);
            MyMediaButtonEventHandler myMediaButtonEventHandler = new MyMediaButtonEventHandler();

            this.mediaSessionConnector = new MediaSessionConnector(mediaSession);
            this.mediaSessionConnector.setPlayer(exoPlayer);
            this.mediaSessionConnector.setPlaybackPreparer(myPlaybackPreparer);
            this.mediaSessionConnector.setControlDispatcher(new DefaultControlDispatcher());
            this.mediaSessionConnector.setQueueNavigator(new MyTimelineQueueNavigator(mediaSession));
            this.mediaSessionConnector.setMediaButtonEventHandler(myMediaButtonEventHandler);
            this.mediaSessionConnector.setEnabledPlaybackActions(SUPPORTED_PLAYBACK_ACTIONS);

            setSessionToken(mediaSession.getSessionToken());
            //mediaSession.setCallback(mediaSessionCallback);
        });
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
        mediaSession.release();
        worker.quitSafely();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        mediaSession.release();
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
    public void setMediaSessionCallback(MediaSessionCallback mediaSessionCallback) {
        this.mediaSessionCallback = mediaSessionCallback;
    }
    @Inject
    public void setWorker(HandlerThread handlerThread) {
        this.worker = handlerThread;
    }

    @Inject
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Inject
    public void setRootAuthenticator(RootAuthenticator rootAuthenticator) {
        this.rootAuthenticator = rootAuthenticator;
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