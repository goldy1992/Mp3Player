package com.github.goldy1992.mp3player.client.dagger.components.fragments

import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.client.views.fragments.MediaControlsFragment
import com.github.goldy1992.mp3player.client.views.fragments.PlayToolbarFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface PlaybackButtonsSubComponent {

    fun inject(mediaControlsFragment: MediaControlsFragment)
    fun inject(playToolBarFragment: PlayToolbarFragment)
}