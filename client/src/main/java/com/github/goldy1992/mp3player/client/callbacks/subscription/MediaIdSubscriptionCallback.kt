package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.support.v4.media.MediaBrowserCompat
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MediaIdSubscriptionCallback

    @Inject
    constructor() : MediaBrowserCompat.SubscriptionCallback() {

    private val mediaBrowserResponseListeners: MutableMap<String, MutableSet<MediaBrowserResponseListener>>

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

    companion object {
        private const val LOG_TAG = "SUBSCRIPTION_CALLBACK"
    }

    init {
        mediaBrowserResponseListeners = HashMap()

    }
}