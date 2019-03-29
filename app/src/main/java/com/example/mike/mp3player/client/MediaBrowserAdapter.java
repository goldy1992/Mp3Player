package com.example.mike.mp3player.client;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.client.callbacks.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.GenericSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.CategorySubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.NotifyAllSubscriptionCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.commons.Range;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryId;
import com.example.mike.mp3player.service.MediaPlaybackService;

import static com.example.mike.mp3player.commons.Constants.PARENT_ID;
import static com.example.mike.mp3player.commons.Constants.RANGE;

public class MediaBrowserAdapter {

    private static final String LOG_TAG = "MDIA_BRWSR_ADPTR";
    private MediaBrowserCompat mMediaBrowser;
    private MyConnectionCallback mConnectionCallbacks;
    private GenericSubscriptionCallback mySubscriptionCallback;
    private Context context;
    private final MediaBrowserConnectorCallback mediaBrowserConnectorCallback;
    private Looper looper;

    public MediaBrowserAdapter(Context context, MediaBrowserConnectorCallback mediaBrowserConnectorCallback, Looper looper) {
        this.context = context;
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
        this.looper = looper;
    }

    public void init(SubscriptionType subscriptionType) {
        mConnectionCallbacks = new MyConnectionCallback(mediaBrowserConnectorCallback);
        ComponentName componentName = new ComponentName(getContext(), MediaPlaybackService.class);
        // Create MediaBrowserServiceCompat
        mMediaBrowser = new MediaBrowserCompat(getContext(), componentName, mConnectionCallbacks, null);
        this.mySubscriptionCallback = new CategorySubscriptionCallback(this);
        createSubscriptionCallback(subscriptionType);
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
     * subscribes to a MediaItem via a libraryId. The id of the libraryId will be used for the parent
     * ID when communicating with the MediaPlaybackService.
     * @param libraryId the libraryId
     */
    public void subscribe(LibraryId libraryId) {
        Bundle options = new Bundle();
        options.putParcelable(PARENT_ID, libraryId);
        getmMediaBrowser().subscribe(libraryId.getId(), options, mySubscriptionCallback);
    }
    /**
     * We should subscribe to MediaItems using LibraryIds in order to avoid having to parse String ids.
     * @param category category
     */
    @Deprecated
    public void subscribe(Category category) {
        this.subscribe(category, null);
    }

    /**
     * we should subscribe to MediaItems using LibraryIds in order to avoid having to parse String ids.
     * @param category category
     * @param id id
     */
    @Deprecated
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
        return mMediaBrowser != null && mMediaBrowser.isConnected();
    }

    public void registerListener(Category category, MediaBrowserResponseListener mediaBrowserResponseListener) {
        mySubscriptionCallback.registerMediaBrowserResponseListener(category, mediaBrowserResponseListener);
    }

    public void unregisterListener(Category category, MediaBrowserResponseListener mediaBrowserResponseListener) {
        mySubscriptionCallback.removeMediaBrowserResponseListener(category, mediaBrowserResponseListener);
    }

    public Context getContext() {
        return context;
    }

    public Looper getLooper() {
        return looper;
    }

    private void createSubscriptionCallback(SubscriptionType subscriptionType) {
        if (null != subscriptionType) {
            switch (subscriptionType) {
                case CATEGORY: this.mySubscriptionCallback = new CategorySubscriptionCallback(this);
                    break;
                default: this.mySubscriptionCallback = new NotifyAllSubscriptionCallback(this);
            }
        }
    }
}
