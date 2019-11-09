package com.github.goldy1992.mp3player.dagger.components;

import android.content.Context;

import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback;
import com.github.goldy1992.mp3player.client.activities.FolderActivity;
import com.github.goldy1992.mp3player.client.activities.MainActivity;
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivity;
import com.github.goldy1992.mp3player.client.views.fragments.PlaybackSpeedControlsFragment;
import com.github.goldy1992.mp3player.dagger.components.fragments.ChildViewPagerFragmentSubcomponent;
import com.github.goldy1992.mp3player.dagger.components.fragments.PlaybackButtonsSubComponent;
import com.github.goldy1992.mp3player.dagger.components.fragments.PlaybackTrackerFragmentSubcomponent;
import com.github.goldy1992.mp3player.dagger.modules.AlbumArtPainterModule;
import com.github.goldy1992.mp3player.dagger.modules.ComponentNameModule;
import com.github.goldy1992.mp3player.dagger.modules.MainHandlerModule;
import com.github.goldy1992.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.github.goldy1992.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.github.goldy1992.mp3player.dagger.modules.MediaControllerCallbackModule;
import com.github.goldy1992.mp3player.dagger.modules.MyDrawerListenerModule;
import com.github.goldy1992.mp3player.dagger.modules.service.HandlerThreadModule;
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import dagger.BindsInstance;
import dagger.Component;

@ComponentScope
@Component(modules = {
        AlbumArtPainterModule.class,
        ComponentNameModule.class,
        HandlerThreadModule.class,
        MainHandlerModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerAdapterModule.class,
        MediaControllerCallbackModule.class,
        MyDrawerListenerModule.class
        })
public interface MediaActivityCompatComponent {

    // activities
    void inject(MainActivity mainActivity);
    void inject(MediaPlayerActivity mediaPlayerActivity);
    void inject(FolderActivity folderActivity);

    // fragments
    void inject(PlaybackSpeedControlsFragment playbackSpeedControlsFragment);

    // sub components
    ChildViewPagerFragmentSubcomponent.Factory childViewPagerFragmentSubcomponentFactory();
    PlaybackTrackerFragmentSubcomponent playbackTrackerSubcomponent();
    PlaybackButtonsSubComponent playbackButtonsSubcomponent();
    SearchResultActivitySubComponent searchResultActivitySubComponent();
    SplashScreenEntryActivityComponent.Factory splashScreenEntryActivity();


    @Component.Factory
    interface Factory {
        MediaActivityCompatComponent create(@BindsInstance Context context,
                                            @BindsInstance String workerId,
                                            @BindsInstance MediaBrowserConnectorCallback callback);
    }
}
