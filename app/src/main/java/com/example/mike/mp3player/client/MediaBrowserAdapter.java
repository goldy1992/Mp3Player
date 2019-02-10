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

public class MediaBrowserAdapter {

    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private MySubscriptionCallback mySubscriptionCallback;
    private Context context;
    private final MediaBrowserConnectorCallback mediaBrowserConnectorCallback;

    public MediaBrowserAdapter(Context context, MediaBrowserConnectorCallback mediaBrowserConnectorCallback) {
        this.context = context;
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
    }

    public void init() {
        mConnectionCallbacks = new MyConnectionCallback(mediaBrowserConnectorCallback);
        ComponentName componentName = new ComponentName(getContext(), MediaPlaybackService.class);
        // Create MediaBrowserServiceCompat
        mMediaBrowser = new MediaBrowserCompat(getContext(), componentName, mConnectionCallbacks, null);
        this.mySubscriptionCallback = new MySubscriptionCallback(getContext());
        getmMediaBrowser().connect();
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
        return mMediaBrowser.getSessionToken();
    }

    public String getRootId() {
        return mMediaBrowser.getRoot();
    }

    public boolean isConnected() {
        return mMediaBrowser != null
                ? mMediaBrowser.isConnected()
                : false;
    }

    public void registerListener(MediaBrowserResponseListener mediaBrowserResponseListener) {
        mySubscriptionCallback.registerMediaBrowserResponseListener(mediaBrowserResponseListener);
    }

    public void unregisterListener(MediaBrowserResponseListener mediaBrowserResponseListener) {
        mySubscriptionCallback.removeMediaBrowserResponseListener(mediaBrowserResponseListener);
    }

    public Context getContext() {
        return context;
    }
}
