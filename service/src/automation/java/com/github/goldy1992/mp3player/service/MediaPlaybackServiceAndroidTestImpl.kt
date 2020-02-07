package com.github.goldy1992.mp3player.service

import com.github.goldy1992.mp3player.commons.MikesMp3Player
import com.github.goldy1992.mp3player.service.dagger.components.DaggerServiceComponent
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent

class MediaPlaybackServiceAndroidTestImpl : MediaPlaybackService() {
    override fun onCreate() {
        initialiseDependencies()
        super.onCreate()
    }

    /**
     * TO BE CALLED BEFORE SUPER CLASS
     */
    public override fun initialiseDependencies() {
        val app = applicationContext as MikesMp3Player
        val component: ServiceComponent = DaggerServiceComponent
                .factory()
                .create(applicationContext, this, "MEDIA_PLYBK_SRVC_WKR", app.getComponentClassMapper())
        component.inject(this)
    }
}