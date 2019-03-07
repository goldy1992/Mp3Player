package com.example.mike.mp3player.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;
import com.example.mike.mp3player.service.library.MediaLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.media.MediaBrowserServiceCompat;

import static com.example.mike.mp3player.commons.Constants.PARENT_ID;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaPlaybackService extends MediaBrowserServiceCompat {

    private static final String MY_MEDIA_ROOT_ID = Category.ROOT.name();
    private static final String MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id";
    private MyNotificationManager notificationManager;
    private MediaSessionCompat mMediaSession;
    private MediaSessionCallback mediaSessionCallback;
    private ServiceManager serviceManager;
    private static final String LOG_TAG = "MEDIA_PLAYBACK_SERVICE";
    private static final String WORKER_ID = "MDIA_PLYBK_SRVC_WKR";
    private MediaLibrary mediaLibrary;
    private HandlerThread worker;

    @Override
    public void onCreate() {
        super.onCreate();
        worker = new HandlerThread(WORKER_ID);
        worker.start();
        worker.getLooper().setMessageLogging((String x) -> {
            Log.i(WORKER_ID, x);
        });
        mediaLibrary = new MediaLibrary(getBaseContext());
        mediaLibrary.init();
        mMediaSession = new MediaSessionCompat(getApplicationContext(), LOG_TAG);
        setSessionToken(mMediaSession.getSessionToken());
        notificationManager = new MyNotificationManager(this);
        serviceManager = new ServiceManager(this, getApplicationContext(), mMediaSession, notificationManager);
        mediaSessionCallback = new MediaSessionCallback(getApplicationContext(), notificationManager, serviceManager, mMediaSession, mediaLibrary, worker.getLooper());
        mediaSessionCallback.init();
        // MySessionCallback() has methods that handle callbacks from a media controller
        mMediaSession.setCallback(mediaSessionCallback);
    }

    @Override
    public BrowserRoot onGetRoot(String clientPackageName, int clientUid,
                                 Bundle rootHints) {

//        // (Optional) Control the level of access for the specified package name.
//        // You'll need to write your own logic to do this.
//        if (allowBrowsing(clientPackageName, clientUid)) {
//            // Returns a root ID that clients can use with onLoadChildren() to retrieve
//            // the content hierarchy.
//            return new BrowserRoot(MY_MEDIA_ROOT_ID, null);
//        } else {
//            // Clients can connect, but this BrowserRoot is an empty hierachy
//            // so onLoadChildren returns nothing. This disables the ability to browse for content.
            return new BrowserRoot(MY_MEDIA_ROOT_ID, null);

//        }
    }

    /**
     * onLoadChildren(String, Result, Bundle) :- onLoadChildren should always be called with a LibraryId item as a bundle option. Searching for
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
    public void onLoadChildren(final String parentMediaId,
                               final Result<List<MediaBrowserCompat.MediaItem>> result, Bundle options) {
        if (options == null) {
            result.sendResult(null);
            return;
        }

        LibraryId libraryId = (LibraryId) options.get(PARENT_ID);
        if (libraryId == null) {
            result.sendResult(null);
            return;
        }


        //  Browsing not allowed
        if (TextUtils.equals(MY_EMPTY_MEDIA_ROOT_ID, parentMediaId)) {
            result.sendResult(null);
            return;
        }
        // Assume for example that the music catalog is already loaded/cached.
        TreeSet<MediaBrowserCompat.MediaItem> mediaItems = mediaLibrary.getChildren(libraryId);
        result.sendResult(new ArrayList<>(mediaItems));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.onDestroy();
        mMediaSession.release();
        worker.quitSafely();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        notificationManager.onDestroy();
        mMediaSession.release();
        stopSelf();
    }

}