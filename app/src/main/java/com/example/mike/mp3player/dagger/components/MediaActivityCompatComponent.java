package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.activities.FolderActivity;
import com.example.mike.mp3player.client.activities.MainActivity;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.activities.SearchResultActivity;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.AlbumArtFragment;
import com.example.mike.mp3player.client.views.fragments.MediaControlsFragment;
import com.example.mike.mp3player.client.views.fragments.MetadataTitleBarFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackSpeedControlsFragment;
import com.example.mike.mp3player.client.views.fragments.TrackInfoFragment;
import com.example.mike.mp3player.dagger.components.fragments.ChildViewPagerFragmentSubcomponent;
import com.example.mike.mp3player.dagger.components.fragments.MainFrameFragmentSubcomponent;
import com.example.mike.mp3player.dagger.components.fragments.PlaybackButtonsSubComponent;
import com.example.mike.mp3player.dagger.components.fragments.PlaybackTrackerFragmentSubcomponent;
import com.example.mike.mp3player.dagger.modules.AlbumArtPainterModule;
import com.example.mike.mp3player.dagger.modules.ChildViewPagerFragmentModule;
import com.example.mike.mp3player.dagger.modules.MainHandlerModule;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.scopes.ComponentScope;

import dagger.BindsInstance;
import dagger.Component;

@ComponentScope
@Component(modules = {
        AlbumArtPainterModule.class,
        ChildViewPagerFragmentModule.class,
        HandlerThreadModule.class,
        MainHandlerModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerAdapterModule.class,
        MediaControllerCallbackModule.class,
        })
public interface MediaActivityCompatComponent {

    // activities
    void inject(MainActivity mainActivity);
    void inject(MediaPlayerActivity mediaPlayerActivity);
    void inject(FolderActivity folderActivity);
    void inject(SearchResultActivity searchResultActivity);

    // fragments
    void inject(PlaybackSpeedControlsFragment playbackSpeedControlsFragment);
    void inject(TrackInfoFragment trackInfoFragment);
    void inject(MediaControlsFragment mediaControlsFragment);
    void inject(AlbumArtFragment albumArtFragment);
    void inject(MetadataTitleBarFragment metadataTitleBarFragment);

    // sub components
    ChildViewPagerFragmentSubcomponent.Factory childViewPagerFragmentSubcomponentFactory();
    MainFrameFragmentSubcomponent mainFrameFragmentSubcomponent();
    PlaybackTrackerFragmentSubcomponent playbackTrackerSubcomponent();
    PlaybackButtonsSubComponent playbackButtonsSubcomponent();
    SplashScreenEntryActivityComponent.Factory splashScreenEntryActivity();


    @Component.Factory
    interface Factory {
        MediaActivityCompatComponent create(@BindsInstance Context context,
                                            @BindsInstance String workerId,
                                            @BindsInstance SubscriptionType subscriptionType,
                                            @BindsInstance MediaBrowserConnectorCallback callback);
    }
}
