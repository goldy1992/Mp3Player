package com.github.goldy1992.mp3player.client.dagger.components

import com.github.goldy1992.mp3player.TestApplication
import com.github.goldy1992.mp3player.client.dagger.modules.TestAppSubcomponents
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestAppSubcomponents::class])
interface TestAppComponent  {

    fun inject(mikesMp3Player: TestApplication)

    // activities
    fun splashScreen() : SplashScreenEntryActivityComponent.Factory
    fun mediaActivity() : AndroidTestMediaActivityCompatComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance componentClassMapper: ComponentClassMapper) : TestAppComponent
    }

}