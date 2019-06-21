package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.components.DaggerMainActivityComponent;
import com.example.mike.mp3player.dagger.components.MainActivityComponent;
import com.example.mike.mp3player.dagger.modules.ApplicationContextModule;
import com.example.mike.mp3player.dagger.modules.LooperModule;
import com.example.mike.mp3player.dagger.modules.MediaBrowserConnectorCallbackModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.SubscriptionTypeModule;

import javax.inject.Inject;


public abstract class MediaBrowserCreatorActivityCompat extends MediaActivityCompat implements MediaBrowserConnectorCallback {

    private MediaBrowserAdapter mediaBrowserAdapter;
    private static final String LOG_TAG = "MDIA_SBSCRBR_ACTVY_CBK";
    abstract SubscriptionType getSubscriptionType();

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        initialiseDependencies();
    }

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
  //      setMediaControllerAdapter(makeMediaControllerAdapter(this, getWorker().getLooper()));
    }

//    public MediaBrowserAdapter makeMediaBrowserAdapter(Context context, MediaBrowserConnectorCallback callback,
//                                        Looper looper, SubscriptionType subscriptionType) {
//        return new MediaBrowserAdapter(context, callback, looper, subscriptionType);
//    }
//
//    public MediaControllerAdapter makeMediaControllerAdapter(Context context, Looper looper) {
//        return new MediaControllerAdapter(context,looper);
//    }

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

    private void initialiseDependencies() {
        ApplicationContextModule applicationContextModule = new ApplicationContextModule(getApplicationContext());
        LooperModule looperModule = new LooperModule(getWorker().getLooper());
        MediaBrowserConnectorCallbackModule mediaBrowserConnectorCallbackModule = new MediaBrowserConnectorCallbackModule(this);
        MediaControllerAdapterModule mediaControllerAdapterModule = new MediaControllerAdapterModule();
        SubscriptionTypeModule subscriptionTypeModule = new SubscriptionTypeModule(getSubscriptionType());
        MainActivityComponent daggerMainActivityComponent = DaggerMainActivityComponent.builder().applicationContextModule(applicationContextModule)
                .looperModule(looperModule)
                .mediaBrowserConnectorCallbackModule(mediaBrowserConnectorCallbackModule)
                .mediaControllerAdapterModule(mediaControllerAdapterModule)
                .subscriptionTypeModule(subscriptionTypeModule)
                .build();
        daggerMainActivityComponent.inject(this);
    }
}
