package com.github.goldy1992.mp3player.client.callbacks

import android.os.Handler
import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

abstract class AsyncCallback<P : Parcelable?>() {
    fun onStateChanged(state: P) {
        CoroutineScope(Default).launch { processCallback(state) }
    }

    abstract fun processCallback(data: P)

}