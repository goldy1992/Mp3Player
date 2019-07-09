package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.MainActivity;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MyMetaDataCallback;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.scopes.AndroidComponentScope;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@AndroidComponentScope
@Singleton
@Component(modules = {
        HandlerThreadModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerAdapterModule.class,
        MediaControllerCallbackModule.class})
public interface MainActivityComponent {

    MediaBrowserAdapter provideMediaBrowserAdapter();
    MediaControllerAdapter provideMediaControllerAdapter();
    MyMediaControllerCallback provideMyMediaControllerCallback();
    MyMetaDataCallback provideMyMetaDataCallback();
    MyPlaybackStateCallback provideMyPlaybackStateCallback();
    String workerId();

    void inject(MainActivity mainActivity);

    @Component.Factory
    interface Factory {
        MainActivityComponent create(@BindsInstance Context context,
                                     @BindsInstance String workerId,
                                     @BindsInstance SubscriptionType subscriptionType,
                                     @BindsInstance MediaBrowserConnectorCallback callback);
    }
}
