package com.example.mike.mp3player.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.media.MediaBrowserServiceCompat;

import com.example.mike.mp3player.MikesMp3PlayerBase;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.commons.library.LibraryResponse;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.session.MediaSessionCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.ACCEPTED_MEDIA_ROOT_ID;
import static com.example.mike.mp3player.commons.Constants.PACKAGE_NAME;
import static com.example.mike.mp3player.commons.Constants.REJECTED_MEDIA_ROOT_ID;
import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;
import static com.example.mike.mp3player.commons.Constants.RESPONSE_OBJECT;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaPlaybackService extends MediaBrowserServiceCompat {
    private MediaSessionCompat mediaSession;
    private MediaSessionCallback mediaSessionCallback;
    private static final String LOG_TAG = "MEDIA_PLAYBACK_SERVICE";
    private static final String WORKER_ID = "MDIA_PLYBK_SRVC_WKR";
    private MediaLibrary mediaLibrary;
    private HandlerThread worker;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MikesMp3PlayerBase)getApplication()).getServiceComponent().inject(this);

        this.worker = new HandlerThread(WORKER_ID);
        this.getWorker().start();
        this.getWorker().getLooper().setMessageLogging((String x) -> {
            //Log.i(WORKER_ID, x);
        });

        this.mediaLibrary.buildMediaLibrary();
     //   this.mediaSession = new MediaSessionCompat(getApplicationContext(), LOG_TAG);
        setSessionToken(getMediaSession().getSessionToken());
   //     this.mediaSessionCallback = new MediaSessionCallback(this, getMediaSession(), mediaLibrary, worker.getLooper());
        // MySessionCallback() has methods that handle callbacks from a media controller
        getMediaSession().setCallback(mediaSessionCallback);
    }

    @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid,
                                 Bundle rootHints) {
        Bundle extras = new Bundle();
        // (Optional) Control the level of access for the specified package name.
        // You'll need to write your own logic to do this.
        if (allowBrowsing(clientPackageName, clientUid)) {
            // Returns a root ID that clients can use with onLoadChildren() to retrieve
            // the content hierarchy.
            return new BrowserRoot(ACCEPTED_MEDIA_ROOT_ID, extras);
        } else {
            // Clients can connect, but this BrowserRoot is an empty hierachy
            // so onLoadChildren returns nothing. This disables the ability to browse for content.
            return new BrowserRoot(REJECTED_MEDIA_ROOT_ID, extras);
        }
    }

    /**
     * onLoadChildren(String, Result, Bundle) :- onLoadChildren should always be called with a LibraryObject item as a bundle option. Searching for
     * a MediaItem's children is now deprecated as it wasted
     * @param parentId
     * @param result
     */
    @Deprecated
    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
     //   this.onLoadChildren(parentId, result, null);
        Log.e(LOG_TAG, "onLoadChildren called without bundle");
    }

    @Override
    public void onLoadChildren(@NonNull final String parentMediaId,
                               final Result<List<MediaBrowserCompat.MediaItem>> result, Bundle options) {
        //  Browsing not allowed
        if (TextUtils.equals(REJECTED_MEDIA_ROOT_ID, parentMediaId)) {
            result.sendResult(null);
            return;
        }

        if (options == null) {
            result.sendResult(null);
            return;
        }

        LibraryRequest libraryRequest = (LibraryRequest) options.get(REQUEST_OBJECT);
        if (libraryRequest == null) {
            result.sendResult(null);
            return;
        }

        // Assume for example that the music catalog is already loaded/cached.
        TreeSet<MediaBrowserCompat.MediaItem> mediaItems = mediaLibrary.getChildren(libraryRequest);
        LibraryResponse libraryResponse = new LibraryResponse(libraryRequest);
        options.putParcelable(RESPONSE_OBJECT, libraryResponse);
        ArrayList<MediaBrowserCompat.MediaItem> toReturn = new ArrayList<>();
        if (mediaItems != null) {
            toReturn.addAll(mediaItems);
        }
        result.sendResult(toReturn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMediaSession().release();
        getWorker().quitSafely();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        getMediaSession().release();
        stopSelf();
    }

    private boolean allowBrowsing(String clientPackageName, int clientUid) {
        return clientPackageName.equals(PACKAGE_NAME);
    }



    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    @Inject
    public void setMediaSessionCallback(MediaSessionCallback mediaSessionCallback) {
        this.mediaSessionCallback = mediaSessionCallback;
    }

    @Inject
    public void setMediaLibrary(MediaLibrary mediaLibrary) {
        this.mediaLibrary = mediaLibrary;
    }

    @Inject
    public void setMediaSession(MediaSessionCompat mediaSession) {
        this.mediaSession = mediaSession;
    }

    public HandlerThread getWorker() {
        return worker;
    }
}