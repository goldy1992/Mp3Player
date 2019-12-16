package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.support.v4.media.MediaBrowserCompat
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import com.github.goldy1992.mp3player.commons.LogTagger
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MediaIdSubscriptionCallback

    @Inject
    constructor() : MediaBrowserCompat.SubscriptionCallback(), LogTagger {

    private val mediaBrowserResponseListeners: MutableMap<String, MutableSet<MediaBrowserResponseListener>> = HashMap()

    override fun onChildrenLoaded(parentId: String, children: List<MediaBrowserCompat.MediaItem>) {
        var childrenArrayList = ArrayList(children)
        val listenersToNotify: Set<MediaBrowserResponseListener>? = mediaBrowserResponseListeners[parentId]
        if (null != listenersToNotify) {
            for (listener in listenersToNotify) {
                listener.onChildrenLoaded(parentId, childrenArrayList)
            }
        }

    }

    @Synchronized
    fun registerMediaBrowserResponseListener(key: String, listener: MediaBrowserResponseListener?) {
        if (mediaBrowserResponseListeners[key] == null) {
            mediaBrowserResponseListeners[key] = HashSet()
        }
        mediaBrowserResponseListeners[key]!!.add(listener!!)
    }

    @VisibleForTesting
    fun getMediaBrowserResponseListeners(): Map<String, MutableSet<MediaBrowserResponseListener>> {
        return mediaBrowserResponseListeners
    }

    override fun logTag(): String {
        return "SUBSCRIPTION_CALLBACK"
    }
}