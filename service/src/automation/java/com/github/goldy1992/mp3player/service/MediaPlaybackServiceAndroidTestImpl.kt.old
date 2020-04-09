package com.github.goldy1992.mp3player.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.github.goldy1992.mp3player.commons.MikesMp3Player
import com.github.goldy1992.mp3player.dagger.components.AndroidTestServiceComponent
import com.github.goldy1992.mp3player.dagger.components.DaggerAndroidTestServiceComponent

class MediaPlaybackServiceAndroidTestImpl : MediaPlaybackService() {
    override fun onCreate() {
        initialiseDependencies()
        super.onCreate()
    }

    // Binder given to clients
    private val binder = LocalBinder()

    class LocalBinder : Binder() {
        fun getService() : MediaPlaybackServiceAndroidTestImpl {
            return MediaPlaybackServiceAndroidTestImpl@this as MediaPlaybackServiceAndroidTestImpl
        }
    }

    /**
     * TO BE CALLED BEFORE SUPER CLASS
     */
    public override fun initialiseDependencies() {
        val app = applicationContext as MikesMp3Player
        val component: AndroidTestServiceComponent = DaggerAndroidTestServiceComponent
                .factory()
                .create(applicationContext, this, "MEDIA_PLYBK_SRVC_WKR", app.getComponentClassMapper())
        component.inject(this)
    }
}