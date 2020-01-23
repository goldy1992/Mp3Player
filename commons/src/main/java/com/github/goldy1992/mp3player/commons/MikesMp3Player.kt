package com.github.goldy1992.mp3player.commons

import android.app.Application
import com.github.goldy1992.mp3player.commons.dagger.components.AppComponent
import com.github.goldy1992.mp3player.commons.dagger.components.DaggerAppComponent

/**
 * Declared in case need in the future
 */
abstract class MikesMp3Player : Application() {
    var appComponent: AppComponent? = null
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .factory()
                .create(getComponentClassMapper())
        appComponent!!.inject(this)
    }

    abstract fun getComponentClassMapper(): ComponentClassMapper
}