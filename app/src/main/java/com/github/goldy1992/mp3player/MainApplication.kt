package com.github.goldy1992.mp3player


import android.app.Application
import com.github.goldy1992.mp3player.commons.SingletonCoroutineScope
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @SingletonCoroutineScope
    @Inject
    lateinit var scope : CoroutineScope

    override fun onTerminate() {
        super.onTerminate()
        scope.cancel()
    }

}