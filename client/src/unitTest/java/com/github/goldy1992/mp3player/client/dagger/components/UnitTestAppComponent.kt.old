package com.github.goldy1992.mp3player.client.dagger.components

import com.github.goldy1992.mp3player.UnitTestApplication
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface UnitTestAppComponent {

    fun inject(mikesMp3Player: UnitTestApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance componentClassMapper: ComponentClassMapper) : UnitTestAppComponent
    }
}