package com.github.goldy1992.mp3player.client.dagger.integration.components

import com.github.goldy1992.mp3player.IntegrationTestApplication
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface IntegrationTestAppComponent  {

    fun inject(mikesMp3Player: IntegrationTestApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance componentClassMapper: ComponentClassMapper) : IntegrationTestAppComponent
    }

}