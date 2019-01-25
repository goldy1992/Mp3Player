package com.example.mike.mp3player.client;

import android.content.ComponentName;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.client.callbacks.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.MySubscriptionCallback;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.service.MediaPlaybackService;

public class MediaBrowserConnector {

    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private MySubscriptionCallback mySubscriptionCallback;
    private Context context;
    private final MediaBrowserConnectorCallback mediaBrowserConnectorCallback;
    private MediaSessionCompat.Token mediaSessionToken;

    public MediaBrowserConnector(Context context, MediaBrowserConnectorCallback mediaBrowserConnectorCallback) {
        this.context = context;
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
    }

    public void init(MediaSessionCompat.Token token) {
            mConnectionCallbacks = new MyConnectionCallback(this);
            // Create MediaBrowserServiceCompat
            mMediaBrowser = new MediaBrowserCompat(context,
                    new ComponentName(context, MediaPlaybackService.class),
                    mConnectionCallbacks,
                    null);

        this.mySubscriptionCallback = new MySubscriptionCallback(mediaBrowserConnectorCallback);
        if (token == null ) {
            getmMediaBrowser().connect();
        } else{
            onConnected(token);
        }
    }

    public void onConnected(MediaSessionCompat.Token token) {
        if (token == null) {
            token = mMediaBrowser.getSessionToken();
            mMediaBrowser.subscribe(mMediaBrowser.getRoot(), mySubscriptionCallback);
        }
        this.mediaSessionToken = token;

    }

    public MediaBrowserCompat getmMediaBrowser() {
        return mMediaBrowser;
    }

    public void disconnect() {
        getmMediaBrowser().disconnect();
    }

    public void subscribe(Category category) {
        this.subscribe(category, null);
    }

    public void subscribe(Category category, String id) {
        String token = LibraryConstructor.buildId(category, id);
        getmMediaBrowser().subscribe(token, mySubscriptionCallback);
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mediaSessionToken;
    }

    public String getRootId() {
        return mMediaBrowser.getRoot();
    }


}
