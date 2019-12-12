package com.github.goldy1992.mp3player.client.callbacks

import android.os.Parcelable
import com.github.goldy1992.mp3player.commons.LogTagger

abstract class AsyncCallback<P : Parcelable>() : LogTagger {

    fun onStateChanged(state: P) {
        processCallback(state)
    }

    abstract fun processCallback(data: P)

}