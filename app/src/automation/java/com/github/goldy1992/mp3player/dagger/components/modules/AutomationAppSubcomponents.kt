package com.github.goldy1992.mp3player.dagger.components.modules

import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.service.dagger.components.AndroidTestServiceComponent
import dagger.Module

@Module(subcomponents = [
    AndroidTestServiceComponent::class,
    MediaActivityCompatComponent::class,
    SplashScreenEntryActivityComponent::class
])
class AutomationAppSubcomponents {
}