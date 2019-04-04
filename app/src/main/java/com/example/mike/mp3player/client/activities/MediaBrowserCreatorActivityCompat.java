package com.example.mike.mp3player.client.activities;

import android.os.Bundle;
import android.util.Log;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;


public abstract class MediaBrowserCreatorActivityCompat extends MediaActivityCompat implements MediaBrowserConnectorCallback {

    private MediaBrowserAdapter mediaBrowserAdapter;
    private static final String LOG_TAG = "MDIA_SBSCRBR_ACTVY_CBK";
    abstract SubscriptionType getSubscriptionType();

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        initialiseMediaControllerAdapter(mediaBrowserAdapter.getMediaSessionToken());
    }

    void initMediaBrowserService() {
        MediaBrowserAdapter mediaBrowserAdapter = new MediaBrowserAdapter(getApplicationContext(),
                this,
                getWorker().getLooper(),
                getSubscriptionType());
        setMediaBrowserAdapter(mediaBrowserAdapter);
        getMediaBrowserAdapter().init();
        setMediaControllerAdapter(new MediaControllerAdapter(this, getWorker().getLooper()));
    }

    public final MediaBrowserAdapter getMediaBrowserAdapter() {
        return mediaBrowserAdapter;
    }

    public final void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnectionSuspended() { /* TODO: implement onConnectionSuspended */
        Log.i(LOG_TAG, "connection suspended");}

    @Override // MediaBrowserConnectorCallback
    public void onConnectionFailed() {  /* TODO: implement onConnectionFailed */
        Log.i(LOG_TAG, "connection failed");
    }


}
