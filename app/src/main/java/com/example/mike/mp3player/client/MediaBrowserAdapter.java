package com.example.mike.mp3player.client;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.client.callbacks.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.CategorySubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.GenericSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.NotifyAllSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;
import com.example.mike.mp3player.service.MediaPlaybackService;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

public class MediaBrowserAdapter {

    private static final String LOG_TAG = "MDIA_BRWSR_ADPTR";
    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private GenericSubscriptionCallback mySubscriptionCallback;
    private Context context;
    private final MediaBrowserConnectorCallback mediaBrowserConnectorCallback;
    private Looper looper;

    @Inject
    public MediaBrowserAdapter(Context context, MediaBrowserConnectorCallback mediaBrowserConnectorCallback, Looper looper, SubscriptionType subscriptionType) {
        this.context = context;
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
        this.looper = looper;
        this.mySubscriptionCallback = createSubscriptionCallback(subscriptionType);
    }

    public void init() {
        mConnectionCallbacks = new MyConnectionCallback(mediaBrowserConnectorCallback);
        ComponentName componentName = new ComponentName(getContext(), MediaPlaybackService.class);
        // Create MediaBrowserServiceCompat
        mMediaBrowser = new MediaBrowserCompat(getContext(), componentName, mConnectionCallbacks, null);

        //Log.i(LOG_TAG, "calling connect");
        getmMediaBrowser().connect();
    }

    public MediaBrowserCompat getmMediaBrowser() {
        return mMediaBrowser;
    }

    public void disconnect() {
        getmMediaBrowser().disconnect();
    }

    /**
     * subscribes to a MediaItem via a libraryRequest. The id of the libraryRequest will be used for the parent
     * ID when communicating with the MediaPlaybackService.
     * @param libraryRequest the libraryRequest
     */
    public void subscribe(LibraryRequest libraryRequest) {
        Bundle options = new Bundle();
        options.putParcelable(REQUEST_OBJECT, libraryRequest);
        getmMediaBrowser().subscribe(libraryRequest.getId(), options, getMySubscriptionCallback());
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mMediaBrowser.getSessionToken();
    }

    public String getRootId() {
        return mMediaBrowser.getRoot();
    }

    public boolean isConnected() {
        return mMediaBrowser != null && mMediaBrowser.isConnected();
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

    public Context getContext() {
        return context;
    }

    public Looper getLooper() {
        return looper;
    }

    private GenericSubscriptionCallback createSubscriptionCallback(SubscriptionType subscriptionType) {
        if (null != subscriptionType) {
            switch (subscriptionType) {
                case CATEGORY: return new CategorySubscriptionCallback(this);
                case MEDIA_ID: return new MediaIdSubscriptionCallback(this);
                default: return new NotifyAllSubscriptionCallback(this);
            }
        }
        return null;
    }

    public GenericSubscriptionCallback getMySubscriptionCallback() {
        return mySubscriptionCallback;
    }
}
