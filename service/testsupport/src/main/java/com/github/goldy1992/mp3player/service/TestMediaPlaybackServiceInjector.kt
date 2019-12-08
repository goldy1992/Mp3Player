package com.github.goldy1992.mp3player.service

import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.service.dagger.components.DaggerTestServiceComponent

class TestMediaPlaybackServiceInjector : MediaPlaybackService() {
    override fun onCreate() {
        initialiseDependencies()
        super.onCreate()
    }

    /**
     * TO BE CALLED BEFORE SUPER CLASS
     */
    override fun initialiseDependencies() {
        val component = DaggerTestServiceComponent
                .factory()
                .create(applicationContext, this, "MEDIA_PLYBK_SRVC_WKR", ComponentClassMapper.Builder().build())
        component!!.inject(this)
    }
}