package com.github.goldy1992.mp3player.dagger.modules

import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent
import dagger.Module

@Module(subcomponents = [
    MediaActivityCompatComponent::class,
    ServiceComponent::class,
    SplashScreenEntryActivityComponent::class
])
class AppSubcomponents