package com.example.mike.mp3player.dagger.components;

import android.content.Context;
import android.os.Looper;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.MediaBrowserCreatorActivityCompat;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MyMetaDataCallback;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.modules.ApplicationContextModule;
import com.example.mike.mp3player.dagger.modules.LooperModule;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaBrowserConnectorCallbackModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.SubscriptionTypeModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationContextModule.class,
        MediaBrowserAdapterModule.class, MediaControllerAdapterModule.class,
        MediaBrowserConnectorCallbackModule.class, LooperModule.class, SubscriptionTypeModule.class})
public interface MainActivityComponent {

    void inject(Looper looper);
    Looper provideLooper();

    void inject(MediaBrowserAdapter mediaBrowserAdapter);
    MediaBrowserAdapter provideMediaBrowserAdapter();

    void inject(MediaControllerAdapter mediaControllerAdapter);
    MediaControllerAdapter provideMediaControllerAdapter();

    void inject(MediaBrowserConnectorCallback mediaBrowserConnectorCallback);
    MediaBrowserConnectorCallback provideMediaBrowserConnectorCallback();

    void inject(SubscriptionType subscriptionType);
    SubscriptionType provideSubscriptionType();

    void inject(MediaBrowserCreatorActivityCompat mainActivity);

    void inject(MyMetaDataCallback myMetaDataCallback);
    MyMetaDataCallback provideMyMetaDataCallback();

    void inject(MyPlaybackStateCallback myPlaybackStateCallback);
    MyPlaybackStateCallback provideMyPlaybackStateCallback();

    void inject(MyMediaControllerCallback myMediaControllerCallback);
    MyMediaControllerCallback provideMyMediaControllerCallback();
}
