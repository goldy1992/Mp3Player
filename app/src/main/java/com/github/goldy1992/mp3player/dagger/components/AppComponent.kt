package com.github.goldy1992.mp3player.dagger.components

import com.github.goldy1992.mp3player.MainApplication
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {

    fun inject(mikesMp3Player: MainApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance componentClassMapper: ComponentClassMapper) : AppComponent
    }
}