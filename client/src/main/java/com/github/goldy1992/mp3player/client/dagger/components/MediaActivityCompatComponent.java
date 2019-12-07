package com.github.goldy1992.mp3player.client.dagger.components;

import android.content.Context;

import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback;
import com.github.goldy1992.mp3player.client.activities.FolderActivity;
import com.github.goldy1992.mp3player.client.activities.MainActivity;
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivity;
import com.github.goldy1992.mp3player.client.dagger.components.fragments.PlaybackButtonsSubComponent;
import com.github.goldy1992.mp3player.client.dagger.components.fragments.PlaybackTrackerFragmentSubcomponent;
import com.github.goldy1992.mp3player.client.dagger.components.fragments.SearchFragmentSubcomponent;
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule;
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserCompatModule;
import com.github.goldy1992.mp3player.client.views.fragments.PlaybackSpeedControlsFragment;
import com.github.goldy1992.mp3player.commons.ComponentClassMapper;
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope;
import com.github.goldy1992.mp3player.client.dagger.components.fragments.ChildViewPagerFragmentSubcomponent;
import com.github.goldy1992.mp3player.client.dagger.modules.MainHandlerModule;
import com.github.goldy1992.mp3player.client.dagger.modules.MyDrawerListenerModule;
import com.github.goldy1992.mp3player.client.dagger.modules.HandlerThreadModule;

import dagger.BindsInstance;
import dagger.Component;

@ComponentScope
@Component(modules = {
        GlideModule.class,
        HandlerThreadModule.class,
        MainHandlerModule.class,
        MediaBrowserCompatModule.class,
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
    SearchFragmentSubcomponent searchFragmentSubcomponent();


    @Component.Factory
    interface Factory {
        MediaActivityCompatComponent create(@BindsInstance Context context,
                                            @BindsInstance String workerId,
                                            @BindsInstance MediaBrowserConnectorCallback callback,
                                            @BindsInstance ComponentClassMapper componentClassMapper);
    }
}
