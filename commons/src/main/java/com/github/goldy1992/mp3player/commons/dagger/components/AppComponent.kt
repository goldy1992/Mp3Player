package com.github.goldy1992.mp3player.commons.dagger.components

import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.MikesMp3Player
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun inject(mikesMp3Player: MikesMp3Player?)
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance componentClassMapper: ComponentClassMapper?): AppComponent?
    }
}