package com.example.mike.mp3player.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;

import com.example.mike.mp3player.service.library.MediaLibrary;

import java.util.List;
import androidx.media.MediaBrowserServiceCompat;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaPlaybackService extends MediaBrowserServiceCompat {

    private static final String MY_MEDIA_ROOT_ID = "media_root_id";
    private static final String MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id";
    private MyNotificationManager notificationManager;
    private MediaSessionCompat mMediaSession;
    private MediaSessionCallback mediaSessionCallback;
    private ServiceManager serviceManager;
    private static final String LOG_TAG = "MEDIA_PLAYBACK_SERVICE";
    private MediaLibrary mediaLibrary;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaLibrary = new MediaLibrary(getBaseContext());
        mediaLibrary.init();
        mMediaSession = new MediaSessionCompat(getApplicationContext(), LOG_TAG);
        setSessionToken(mMediaSession.getSessionToken());
        notificationManager = new MyNotificationManager(this);
        serviceManager = new ServiceManager(this, getApplicationContext(), mMediaSession, notificationManager);
        mediaSessionCallback = new MediaSessionCallback(getApplicationContext(), notificationManager, serviceManager, mMediaSession, mediaLibrary);
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

    @Override
    public void onLoadChildren(final String parentMediaId,
                               final Result<List<MediaBrowserCompat.MediaItem>> result) {

        //  Browsing not allowed
        if (TextUtils.equals(MY_EMPTY_MEDIA_ROOT_ID, parentMediaId)) {
            result.sendResult(null);
            return;
        }
        // Assume for example that the music catalog is already loaded/cached.
        List<MediaBrowserCompat.MediaItem> mediaItems = mediaLibrary.getLibrary();

        // Check if this is the root menu:
        if (MY_MEDIA_ROOT_ID.equals(parentMediaId)) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...
            result.sendResult(mediaItems);
        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
        }
        //result.sendResult(mediaItems);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.onDestroy();
        mMediaSession.release();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        notificationManager.onDestroy();
        mMediaSession.release();
        stopSelf();
    }

}