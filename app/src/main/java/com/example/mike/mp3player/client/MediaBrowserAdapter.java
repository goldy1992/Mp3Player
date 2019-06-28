package com.example.mike.mp3player.client;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.client.callbacks.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.GenericSubscriptionCallback;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

@Singleton
public class MediaBrowserAdapter {

    private static final String LOG_TAG = "MDIA_BRWSR_ADPTR";
    private MediaBrowserCompat mediaBrowser;
    private MyConnectionCallback connectionCallback;
    private GenericSubscriptionCallback mySubscriptionCallback;

    @Inject
    public MediaBrowserAdapter(MediaBrowserCompat mediaBrowser,
                                MyConnectionCallback myConnectionCallback,
                                GenericSubscriptionCallback mySubscriptionCallback) {
        this.mediaBrowser = mediaBrowser;
        this.connectionCallback = myConnectionCallback;
        this.mySubscriptionCallback = mySubscriptionCallback;
    //  this.mySubscriptionCallback = createSubscriptionCallback(subscriptionType);
    //  ComponentName componentName = new ComponentName(getContext(), MediaPlaybackService.class);
    //  this.mediaBrowser = new MediaBrowserCompat(getContext(), componentName, getConnectionCallback(), null);

    }

    public void init() {
        // Create MediaBrowserServiceCompat
        mediaBrowser.connect();
        //Log.i(LOG_TAG, "calling connect");
    }

    public MediaBrowserCompat getMediaBrowser() {
        return mediaBrowser;
    }

    public void disconnect() {
        mediaBrowser.disconnect();
    }

    /**
     * subscribes to a MediaItem via a libraryRequest. The id of the libraryRequest will be used for the parent
     * ID when communicating with the MediaPlaybackService.
     * @param libraryRequest the libraryRequest
     */
    public void subscribe(LibraryRequest libraryRequest) {
        Bundle options = new Bundle();
        options.putParcelable(REQUEST_OBJECT, libraryRequest);
        mediaBrowser.subscribe(libraryRequest.getId(), options, getMySubscriptionCallback());
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

    public void registerListener(Object parentId, MediaBrowserResponseListener mediaBrowserResponseListener) {
        getMySubscriptionCallback().registerMediaBrowserResponseListener(parentId, mediaBrowserResponseListener);
    }

    @Deprecated
    public void registerListener(Category category, MediaBrowserResponseListener mediaBrowserResponseListener) {
        getMySubscriptionCallback().registerMediaBrowserResponseListener(category, mediaBrowserResponseListener);
    }

    @Deprecated
    public void unregisterListener(Category category, MediaBrowserResponseListener mediaBrowserResponseListener) {
        getMySubscriptionCallback().removeMediaBrowserResponseListener(category, mediaBrowserResponseListener);
    }


    public GenericSubscriptionCallback getMySubscriptionCallback() {
        return mySubscriptionCallback;
    }

    public MyConnectionCallback getConnectionCallback() {
        return connectionCallback;
    }
}
