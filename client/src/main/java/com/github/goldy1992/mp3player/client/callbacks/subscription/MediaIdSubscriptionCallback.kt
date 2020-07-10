package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.support.v4.media.MediaBrowserCompat
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.MediaBrowserSubscriber
import com.github.goldy1992.mp3player.commons.LogTagger
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MediaIdSubscriptionCallback

    @Inject
    constructor() : MediaBrowserCompat.SubscriptionCallback(), LogTagger {

    private val subscribers: MutableMap<String, MutableSet<MediaBrowserSubscriber>> = HashMap()

    override fun onChildrenLoaded(parentId: String, children: List<MediaBrowserCompat.MediaItem>) {
        val childrenArrayList = ArrayList(children)
        val listenersToNotify: Set<MediaBrowserSubscriber>? = subscribers[parentId]
        if (null != listenersToNotify) {
            for (listener in listenersToNotify) {
                listener.onChildrenLoaded(parentId, childrenArrayList)
            }
        }

    }

    @Synchronized
    fun registerMediaBrowserSubscriber(key: String, listener: MediaBrowserSubscriber?) {
        if (subscribers[key] == null) {
            subscribers[key] = HashSet()
        }
        subscribers[key]!!.add(listener!!)
    }

    @VisibleForTesting
    fun getMediaBrowserSubscribers() : Map<String, MutableSet<MediaBrowserSubscriber>> {
        return subscribers
    }

    override fun logTag(): String {
        return "SUBSCRIPTION_CALLBACK"
    }
}