package com.github.goldy1992.mp3player.client.callbacks.metadata

import android.media.MediaMetadata
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject
import kotlin.collections.HashSet

class MyMetadataCallback

    @Inject
    constructor() : LogTagger {

    private var currentMediaId: String? = null

    private val metadataListeners: MutableSet<MetadataListener> = HashSet()

    fun processCallback(data: MediaMetadataCompat) {
        val mediaMetadata = data.mediaMetadata as MediaMetadata
        val newMediaId = mediaMetadata.description.mediaId
        newMediaId?.let {
            if (newMediaId != currentMediaId) {
                currentMediaId = newMediaId
                notifyListeners(data)
            }
        }
    }

    private fun notifyListeners(metadata: MediaMetadataCompat) {
        for (listener in metadataListeners) {
            listener.onMetadataChanged(metadata)
        }
    }

    fun registerMetaDataListener(listener: MetadataListener) {
        Log.i(logTag(), "registerMetaDataListener" + listener.javaClass)
        metadataListeners.add(listener)
    }

    fun removeMetaDataListener(listener: MetadataListener?): Boolean {
        return metadataListeners.remove(listener)
    }

    override fun logTag(): String {
        return "MY_META_DTA_CLLBK"
    }


}