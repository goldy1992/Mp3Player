package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import javax.inject.Inject;


public abstract class MediaBrowserCreatorActivityCompat extends MediaActivityCompat implements MediaBrowserConnectorCallback {

    private MediaBrowserAdapter mediaBrowserAdapter;
    private static final String LOG_TAG = "MDIA_SBSCRBR_ACTVY_CBK";
    abstract SubscriptionType getSubscriptionType();

    @Override // MediaBrowserConnectorCallback
    public void onConnected() {
        initialiseMediaControllerAdapter(mediaBrowserAdapter.getMediaSessionToken());
    }

    void initMediaBrowserService() {
        mediaBrowserAdapter.init();
//        MediaBrowserAdapter mediaBrowserAdapter = makeMediaBrowserAdapter(getApplicationContext(),
//                this,
//                getWorker().getLooper(),
//                getSubscriptionType());
//        setMediaBrowserAdapter(mediaBrowserAdapter);
//        getMediaBrowserAdapter().init();
        setMediaControllerAdapter(makeMediaControllerAdapter(this, getWorker().getLooper()));
    }

    public MediaBrowserAdapter makeMediaBrowserAdapter(Context context, MediaBrowserConnectorCallback callback,
                                        Looper looper, SubscriptionType subscriptionType) {
        return new MediaBrowserAdapter(context, callback, looper, subscriptionType);
    }

    public MediaControllerAdapter makeMediaControllerAdapter(Context context, Looper looper) {
        return new MediaControllerAdapter(context,looper);
    }

    public final MediaBrowserAdapter getMediaBrowserAdapter() {
        return mediaBrowserAdapter;
    }

    @Inject
    public final void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMediaControllerAdapter().disconnect();
        getMediaBrowserAdapter().disconnect();
    }

    @Override // MediaBrowserConnectorCallback
    public void onConnectionSuspended() { /* TODO: implement onConnectionSuspended */
        Log.i(LOG_TAG, "connection suspended");}

    @Override // MediaBrowserConnectorCallback
    public void onConnectionFailed() {  /* TODO: implement onConnectionFailed */
        Log.i(LOG_TAG, "connection failed");
    }


}
