package com.github.goldy1992.mp3player.client.callbacks.metadata

import android.media.MediaMetadata
import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.github.goldy1992.mp3player.client.callbacks.AsyncCallback
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class MyMetadataCallback

    @Inject
    constructor()
    : AsyncCallback<MediaMetadataCompat?>() {

    private var currentMediaId: String? = null
    private val metadataListeners: MutableSet<MetadataListener>
    override fun processCallback(mediaMetadataCompat: MediaMetadataCompat?) {
        if (mediaMetadataCompat == null || mediaMetadataCompat.mediaMetadata == null) {
            return
        }
        val mediaMetadata = mediaMetadataCompat.mediaMetadata as MediaMetadata ?: return
        val newMediaId = mediaMetadata.description.mediaId
        if (newMediaId != null && newMediaId != currentMediaId) {
            currentMediaId = newMediaId
            notifyListeners(mediaMetadataCompat)
        }
    }

    private fun notifyListeners(metadata: MediaMetadataCompat) {
        for (listener in metadataListeners) {
            listener?.onMetadataChanged(metadata)
        }
    }

    @Synchronized
    fun registerMetaDataListener(listener: MetadataListener) {
        Log.i(LOG_TAG, "registerMetaDataListener" + listener.javaClass)
        metadataListeners.add(listener)
    }

    @Synchronized
    fun removeMetaDataListener(listener: MetadataListener?): Boolean {
        return metadataListeners.remove(listener)
    }

    companion object {
        private const val LOG_TAG = "MY_META_DTA_CLLBK"
    }

    init {
        metadataListeners = HashSet()
    }
}