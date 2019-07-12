package com.example.mike.mp3player.dagger.components.fragments;

import android.content.Context;

import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.activities.FolderActivity;
import com.example.mike.mp3player.client.activities.MainActivity;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.PlayToolBarFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackSpeedControlsFragment;
import com.example.mike.mp3player.client.views.fragments.ShuffleRepeatFragment;
import com.example.mike.mp3player.client.views.fragments.TrackInfoFragment;
import com.example.mike.mp3player.dagger.modules.MainHandlerModule;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.scopes.ComponentScope;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@ComponentScope
@Component(modules = {
        HandlerThreadModule.class,
        MainHandlerModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerAdapterModule.class,
        MediaControllerCallbackModule.class})
public interface MediaActivityCompatComponent {

    // activities
    void inject(MainActivity mainActivity);
    void inject(MediaPlayerActivity mediaPlayerActivity);
    void inject(FolderActivity folderActivity);

    // fragments
    void inject(PlayToolBarFragment fragment);
    void inject(PlaybackSpeedControlsFragment playbackSpeedControlsFragment);
    void inject(TrackInfoFragment trackInfoFragment);
    void inject(ShuffleRepeatFragment shuffleRepeatFragment);

    // subcomponents
    PlaybackTrackerFragmentSubcomponent playbackTrackerSubcomponent();

    @Component.Factory
    interface Factory {
        MediaActivityCompatComponent create(@BindsInstance Context context,
                                            @BindsInstance String workerId,
                                            @BindsInstance SubscriptionType subscriptionType,
                                            @BindsInstance MediaBrowserConnectorCallback callback);
    }
}
