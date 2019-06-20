package com.example.mike.mp3player.dagger.components;

import android.content.Context;
import android.os.Looper;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.activities.MainActivity;
import com.example.mike.mp3player.client.activities.MediaBrowserCreatorActivityCompat;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.modules.ApplicationContextModule;
import com.example.mike.mp3player.dagger.modules.LooperModule;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaBrowserConnectorCallbackModule;
import com.example.mike.mp3player.dagger.modules.SubscriptionTypeModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationContextModule.class,
        MediaBrowserAdapterModule.class, MediaBrowserConnectorCallbackModule.class,
        LooperModule.class, SubscriptionTypeModule.class})
public interface MainActivityComponent {

    Context provideContext();
    Looper provideLooper();
    MediaBrowserAdapter provideMediaBrowserAdapter();
    MediaBrowserConnectorCallback provideMediaBrowserConnectorCallback();
    SubscriptionType provideSubscriptionType();


    void inject(Context context);
    void inject(Looper looper);
    void inject(MediaBrowserAdapter mediaBrowserAdapter);
    void inject(MediaBrowserConnectorCallback mediaBrowserConnectorCallback);
    void inject(SubscriptionType subscriptionType);
    void inject(MediaBrowserCreatorActivityCompat mainActivity);
}
