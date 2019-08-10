package com.example.mike.mp3player.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.media.MediaBrowserServiceCompat;

import com.example.mike.mp3player.service.library.ContentManager;
import com.example.mike.mp3player.service.session.MediaSessionCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.PACKAGE_NAME;

/**
 * Created by Mike on 24/09/2017.
 */
public abstract class MediaPlaybackService extends MediaBrowserServiceCompat {

    private static final String LOG_TAG = "MEDIA_PLAYBACK_SERVICE";

    private ContentManager mediaLibrary;
    private HandlerThread worker;
    private Handler handler;
    private MediaSessionCompat mediaSession;
    private MediaSessionCallback mediaSessionCallback;
    private RootAuthenticator rootAuthenticator;
    abstract void initialiseDependencies();

    @Override
    public void onCreate() {
        super.onCreate();
        this.handler = new Handler(worker.getLooper());
        handler.post(() -> {
           // this.mediaLibrary.buildDbMediaLibrary();
            this.mediaSessionCallback.init();
            setSessionToken(mediaSession.getSessionToken());
            mediaSession.setCallback(mediaSessionCallback);
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
        if (!rootAuthenticator.allowSubscription(parentId)) {
            result.sendResult(null);
            return;
        }

        result.detach();
        handler.post(() -> {
            // Assume for example that the music catalog is already loaded/cached.
            List<MediaBrowserCompat.MediaItem> mediaItems = mediaLibrary.getChildren(parentId);
            ArrayList<MediaBrowserCompat.MediaItem> toReturn = new ArrayList<>();
            if (mediaItems != null) {
                toReturn.addAll(mediaItems);
            }
            result.sendResult(toReturn);
        });

    }

    @Override
    public void onSearch(@NonNull String query, Bundle extras,
                         @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        Log.i(LOG_TAG, "hit the search function");
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder().setMediaId("mediaId").build();
        MediaBrowserCompat.MediaItem mediaItem = new MediaBrowserCompat.MediaItem(mediaDescriptionCompat, 0);

        result.sendResult(Collections.singletonList(mediaItem));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getMediaSession().release();
        worker.quitSafely();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        getMediaSession().release();
        stopSelf();
    }

    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    @Inject
    public void setMediaLibrary(ContentManager mediaLibrary) {
        this.mediaLibrary = mediaLibrary;
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
    public void setRootAuthenticator(RootAuthenticator rootAuthenticator) {
        this.rootAuthenticator = rootAuthenticator;
    }

    public HandlerThread getWorker() {
        return worker;
    }
}