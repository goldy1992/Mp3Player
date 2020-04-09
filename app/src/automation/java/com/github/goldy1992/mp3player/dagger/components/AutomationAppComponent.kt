package com.github.goldy1992.mp3player.dagger.components

import com.github.goldy1992.mp3player.dagger.components.modules.AutomationAppSubcomponents
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AutomationAppSubcomponents::class])
interface AutomationAppComponent : AppComponent {

    @Component.Factory
    interface Factory : AppComponent.Factory

}