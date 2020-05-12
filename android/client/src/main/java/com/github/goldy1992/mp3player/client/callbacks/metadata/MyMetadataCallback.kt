package com.github.goldy1992.mp3player.client.callbacks.metadata

import android.media.MediaMetadata
import android.support.v4.media.MediaMetadataCompat
import com.github.goldy1992.mp3player.client.callbacks.Callback
import com.github.goldy1992.mp3player.client.callbacks.Listener
import javax.inject.Inject

class MyMetadataCallback

    @Inject
    constructor() : Callback() {

    private var currentMediaId: String? = null

    override fun processCallback(data: Any) {
        val mediaMetadata = (data as MediaMetadataCompat).mediaMetadata as MediaMetadata
        val newMediaId = mediaMetadata.description.mediaId
        newMediaId?.let {
            if (newMediaId != currentMediaId) {
                currentMediaId = newMediaId
                super.processCallback(data)
            }
        }
    }

    override fun updateListener(listener: Listener, data: Any) {
        (listener as MetadataListener).onMetadataChanged(data as MediaMetadataCompat)
    }

    override fun logTag(): String {
        return "MY_META_DTA_CLLBK"
    }


}