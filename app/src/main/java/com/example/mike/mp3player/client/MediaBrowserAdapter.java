package com.example.mike.mp3player.client;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.client.callbacks.MyConnectionCallback;
import com.example.mike.mp3player.client.callbacks.MySubscriptionCallback;
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
    private MySubscriptionCallback mySubscriptionCallback;
    private Context context;
    private final MediaBrowserConnectorCallback mediaBrowserConnectorCallback;
    private Looper looper;

    public MediaBrowserAdapter(Context context, MediaBrowserConnectorCallback mediaBrowserConnectorCallback, Looper looper) {
        this.context = context;
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback;
        this.looper = looper;
    }

    public void init() {
        mConnectionCallbacks = new MyConnectionCallback(mediaBrowserConnectorCallback);
        ComponentName componentName = new ComponentName(getContext(), MediaPlaybackService.class);
        // Create MediaBrowserServiceCompat
        mMediaBrowser = new MediaBrowserCompat(getContext(), componentName, mConnectionCallbacks, null);
        this.mySubscriptionCallback = new MySubscriptionCallback(getContext(), this.looper);
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
    public void subscribe(LibraryId libraryId, Range range) {
        Bundle options = new Bundle();
        options.putParcelable(RANGE, range);
        options.putParcelable(PARENT_ID, libraryId);
        getmMediaBrowser().subscribe(libraryId.getId(), options, mySubscriptionCallback);
    }
    public void subscribe(LibraryId libraryId)
    {
        subscribe(libraryId, null);
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
