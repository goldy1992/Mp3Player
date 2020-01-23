package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.dagger.components.DaggerSplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.client.dagger.components.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.service.MediaPlaybackServiceInjector

class SplashScreenEntryActivityInjector : SplashScreenEntryActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun initialiseDependencies() {
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder()
                .service(MediaPlaybackServiceInjector::class.java)
                .mainActivity(MainActivityInjector::class.java)
                .build()
        val component : SplashScreenEntryActivityComponent = DaggerSplashScreenEntryActivityComponent
                .factory()
                .create(this, this, componentClassMapper)
        component.inject(this)
    }
}