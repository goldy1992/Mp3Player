package com.github.goldy1992.mp3player.client.dagger.components

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.activities.FolderActivity
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivity
import com.github.goldy1992.mp3player.client.dagger.components.fragments.ChildViewPagerFragmentSubcomponent
import com.github.goldy1992.mp3player.client.dagger.components.fragments.PlaybackButtonsSubComponent
import com.github.goldy1992.mp3player.client.dagger.components.fragments.PlaybackTrackerFragmentSubcomponent
import com.github.goldy1992.mp3player.client.dagger.components.fragments.SearchFragmentSubcomponent
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserCompatModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerModule
import com.github.goldy1992.mp3player.client.views.fragments.PlaybackSpeedControlsFragment
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.BindsInstance
import dagger.Component

@ComponentScope
@Component(modules =
    [GlideModule::class,
    MediaBrowserCompatModule::class])
interface MediaActivityCompatComponent {
    // activities
    fun inject(mainActivity: MainActivity)
    // Media player activity
    fun inject(mediaPlayerActivity: MediaPlayerActivity)
    // Folder Actviity
    fun inject(folderActivity: FolderActivity)
    // fragments
    fun inject(playbackSpeedControlsFragment: PlaybackSpeedControlsFragment)

    // sub components
    fun childViewPagerFragmentSubcomponentFactory(): ChildViewPagerFragmentSubcomponent.Factory

    fun playbackTrackerSubcomponent(): PlaybackTrackerFragmentSubcomponent
    fun playbackButtonsSubcomponent(): PlaybackButtonsSubComponent
    fun searchResultActivitySubComponent(): SearchResultActivitySubComponent
    fun searchFragmentSubcomponent(): SearchFragmentSubcomponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context,
                   @BindsInstance callback: MediaBrowserConnectorCallback,
                   @BindsInstance componentClassMapper: ComponentClassMapper): MediaActivityCompatComponent
    }
}