package com.github.goldy1992.mp3player.client.dagger.subcomponents

import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.client.views.fragments.SearchFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface SearchFragmentSubcomponent {

    fun inject(fragment: SearchFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create() : SearchFragmentSubcomponent
    }

}