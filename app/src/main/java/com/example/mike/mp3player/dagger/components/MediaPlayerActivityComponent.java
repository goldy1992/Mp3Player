package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {HandlerThreadModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerAdapterModule.class})
public interface MediaPlayerActivityComponent {

    MediaControllerAdapter provideMediaControllerAdapter();
    MediaBrowserAdapter provideMediaBrowserAdapter();

    void inject(MediaPlayerActivity activity);

    @Component.Factory
    interface Factory {
        MediaPlayerActivityComponent create(@BindsInstance Context context,
                                            @BindsInstance String workerId,
                                            @BindsInstance SubscriptionType subscriptionType,
                                            @BindsInstance MediaBrowserConnectorCallback callback);
    }
}
