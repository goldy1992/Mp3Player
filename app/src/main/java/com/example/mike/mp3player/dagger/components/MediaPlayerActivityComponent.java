package com.example.mike.mp3player.dagger.components;

import android.content.Context;
import android.os.Handler;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.components.fragments.PlaybackSpeedControlsFragmentSubcomponent;
import com.example.mike.mp3player.dagger.components.fragments.PlaybackTrackerFragmentSubcomponent;
import com.example.mike.mp3player.dagger.components.fragments.ShuffleRepeatFragmentSubcomponent;
import com.example.mike.mp3player.dagger.components.fragments.TrackInfoFragmentSubcomponent;
import com.example.mike.mp3player.dagger.modules.MainHandlerModule;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.scopes.AndroidComponentScope;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Component;

@AndroidComponentScope
@Component(modules = {HandlerThreadModule.class,
        MainHandlerModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerAdapterModule.class})
public interface MediaPlayerActivityComponent {

    MediaControllerAdapter provideMediaControllerAdapter();
    MediaBrowserAdapter provideMediaBrowserAdapter();

    @Named("main") Handler provideMainHandler();
    PlaybackTrackerFragmentSubcomponent providePlaybackTrackerFragment();
    PlaybackSpeedControlsFragmentSubcomponent providePlaybackSpeedControlsFragmentSubcomponent();
    TrackInfoFragmentSubcomponent provideTrackInfoSubcomponent();
    ShuffleRepeatFragmentSubcomponent provideShuffleRepeatFragment();

    void inject(MediaPlayerActivity activity);

    @Component.Factory
    interface Factory {
        MediaPlayerActivityComponent create(@BindsInstance Context context,
                                            @BindsInstance String workerId,
                                            @BindsInstance SubscriptionType subscriptionType,
                                            @BindsInstance MediaBrowserConnectorCallback callback);
    }
}
