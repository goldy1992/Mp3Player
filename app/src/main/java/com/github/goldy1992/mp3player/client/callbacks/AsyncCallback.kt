package com.github.goldy1992.mp3player.client.callbacks

import android.os.Handler
import android.os.Parcelable

abstract class AsyncCallback<P : Parcelable?>(var worker: Handler) {
    fun onStateChanged(state: P) {
        worker.post { processCallback(state) }
    }

    abstract fun processCallback(data: P)

}