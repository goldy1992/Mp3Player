package com.github.goldy1992.mp3player.dagger.modules

import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import dagger.Module

@Module(subcomponents = [
    MediaActivityCompatComponent::class,
    SplashScreenEntryActivityComponent::class
])
class AppSubcomponents