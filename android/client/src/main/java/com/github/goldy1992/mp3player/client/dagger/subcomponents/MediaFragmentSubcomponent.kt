package com.github.goldy1992.mp3player.client.dagger.subcomponents

import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.client.views.fragments.*
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface MediaFragmentSubcomponent {

    fun inject(mediaControlsFragment: MediaControlsFragment)
    fun inject(playbackSpeedControlsFragment: PlaybackSpeedControlsFragment)
    fun inject(playbackTrackerFragment: PlaybackTrackerFragment)
    fun inject(playToolbarFragment: PlayToolbarFragment)
    fun inject(searchFragment: SearchFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create() : MediaFragmentSubcomponent
    }
}