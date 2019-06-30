package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.FolderActivity;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MyMetaDataCallback;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        HandlerThreadModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerAdapterModule.class})
public interface FolderActivityComponent {

    MediaBrowserAdapter provideMediaBrowserAdapter();
    MediaControllerAdapter provideMediaControllerAdapter();
    MyMediaControllerCallback provideMyMediaControllerCallback();
    MyMetaDataCallback provideMyMetaDataCallback();
    MyPlaybackStateCallback provideMyPlaybackStateCallback();
    String workerId();

    void inject(FolderActivity activity);

    @Component.Factory
    interface Factory {
        FolderActivityComponent create(@BindsInstance Context context,
                                     @BindsInstance String workerId,
                                     @BindsInstance SubscriptionType subscriptionType,
                                     @BindsInstance MediaBrowserConnectorCallback callback);
    }
}
