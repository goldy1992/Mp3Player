package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.dagger.components.DaggerSplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.client.dagger.components.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.MikesMp3Player
import com.github.goldy1992.mp3player.service.MediaPlaybackServiceInjector

class SplashScreenEntryActivityInjector : SplashScreenEntryActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun initialiseDependencies() {
        val app : MikesMp3Player = applicationContext!! as MikesMp3Player
        val component : SplashScreenEntryActivityComponent = DaggerSplashScreenEntryActivityComponent
                .factory()
                .create(this, this, app.getComponentClassMapper())
        component.inject(this)
    }
}