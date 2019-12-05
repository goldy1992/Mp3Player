package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import javax.inject.Inject
import javax.inject.Named

abstract class AsyncFragment : Fragment() {
    @JvmField
    var mainUpdater: Handler
    @JvmField
    var worker: Handler? = null
    @Inject
    fun setMainUpdater(@Named("main") mainUpdater: Handler, @Named("worker") worker: Handler?) {
        this.mainUpdater = mainUpdater
        this.worker = worker
    }

    init {
        mainUpdater = Handler(Looper.getMainLooper())
    }
}