package com.github.goldy1992.mp3player.dagger.components

import com.github.goldy1992.mp3player.MainApplication
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.dagger.modules.AppSubcomponents
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class])
interface AppComponent {

    fun inject(mikesMp3Player: MainApplication)

    // activities
    fun splashScreen() : SplashScreenEntryActivityComponent.Factory
    fun mediaActivity() : MediaActivityCompatComponent.Factory

    // services
    fun mediaPlaybackService() : ServiceComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance componentClassMapper: ComponentClassMapper) : AppComponent
    }
}