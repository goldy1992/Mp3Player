package com.example.mike.mp3player.client;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.client.callbacks.connection.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.search.MySearchCallback;
import com.example.mike.mp3player.client.callbacks.search.SearchResultListener;
import com.example.mike.mp3player.client.callbacks.subscription.GenericSubscriptionCallback;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MediaBrowserAdapter {

    private static final String LOG_TAG = "MDIA_BRWSR_ADPTR";
    private MediaBrowserCompat mediaBrowser;
    private MyConnectionCallback connectionCallback;
    private GenericSubscriptionCallback mySubscriptionCallback;
    private MySearchCallback mySearchCallback;

    @Inject
    public MediaBrowserAdapter(MediaBrowserCompat mediaBrowser,
                               MyConnectionCallback myConnectionCallback,
                               GenericSubscriptionCallback mySubscriptionCallback,
                               MySearchCallback mySearchCallback) {
        this.mediaBrowser = mediaBrowser;
        this.connectionCallback = myConnectionCallback;
        this.mySubscriptionCallback = mySubscriptionCallback;
        this.mySearchCallback = mySearchCallback;
    }

    public void init() {
        // Create MediaBrowserServiceCompat
        mediaBrowser.connect();
        //Log.i(LOG_TAG, "calling connect");
    }

    /**
     * Disconnects from the media browser service
     */
    public void disconnect() {
        mediaBrowser.disconnect();
    }

    public void search(String query, Bundle extras) {
        mediaBrowser.search(query, extras, mySearchCallback);
    }
    /**
     * Connects to the media browser service
     */
    public void connect() {
        mediaBrowser.connect();
    }

    /**
     * subscribes to a MediaItem via a libraryRequest. The id of the libraryRequest will be used for the parent
     * ID when communicating with the MediaPlaybackService.
     * @param id the id of the media item to be subscribed to
     */
    public void subscribe(String id) {
        mediaBrowser.subscribe(id, mySubscriptionCallback);
    }

    public void subscribeToRoot() {
        mediaBrowser.subscribe(getRootId(), mySubscriptionCallback);
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mediaBrowser.getSessionToken();
    }

    public String getRootId() {
        return mediaBrowser.getRoot();
    }

    public boolean isConnected() {
        return mediaBrowser != null && mediaBrowser.isConnected();
    }

    public void registerRootListener(MediaBrowserResponseListener mediaBrowserResponseListener) {
        mySubscriptionCallback.registerMediaBrowserResponseListener(getRootId(), mediaBrowserResponseListener);
    }

    public void registerListener(String parentId, MediaBrowserResponseListener mediaBrowserResponseListener) {
        mySubscriptionCallback.registerMediaBrowserResponseListener(parentId, mediaBrowserResponseListener);
    }

    public void registerSearchResultListener(SearchResultListener searchResultListener) {
        this.mySearchCallback.registerSearchResultListener(searchResultListener);
    }

    public boolean unregisterSearchResultListener(SearchResultListener searchResultListener) {
        return this.mySearchCallback.unregisterSearchResultListener(searchResultListener);
    }

    public GenericSubscriptionCallback getMySubscriptionCallback() {
        return mySubscriptionCallback;
    }

    public MyConnectionCallback getConnectionCallback() {
        return connectionCallback;
    }
}
