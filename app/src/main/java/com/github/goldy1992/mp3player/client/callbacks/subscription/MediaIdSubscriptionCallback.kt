package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class MediaIdSubscriptionCallback @Inject constructor(@Named("worker") handler: Handler) : MediaBrowserCompat.SubscriptionCallback() {
    private val handler: Handler
    private val mediaBrowserResponseListeners: MutableMap<String, MutableSet<MediaBrowserResponseListener>>
    override fun onChildrenLoaded(parentId: String, children: List<MediaBrowserCompat.MediaItem>) {
        handler.post {
            val childrenArrayList = ArrayList(children)
            val listenersToNotify: Set<MediaBrowserResponseListener>? = mediaBrowserResponseListeners[parentId]
            if (null != listenersToNotify) {
                for (listener in listenersToNotify) {
                    listener.onChildrenLoaded(parentId, childrenArrayList)
                }
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
        this.handler = handler
    }
}