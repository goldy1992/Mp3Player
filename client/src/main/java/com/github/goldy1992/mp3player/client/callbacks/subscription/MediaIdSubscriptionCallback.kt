package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.support.v4.media.MediaBrowserCompat
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.MediaBrowserSubscriber
import com.github.goldy1992.mp3player.commons.LogTagger
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MediaIdSubscriptionCallback

    @Inject
    constructor() : MediaBrowserCompat.SubscriptionCallback(), LogTagger {

    private val rootLiveData = MutableLiveData<List<MediaBrowserCompat.MediaItem>>()

    private val subscribers: MutableMap<String, MutableSet<MediaBrowserSubscriber>> = HashMap()

    private val currentData : MutableMap<String, LiveData<List<MediaBrowserCompat.MediaItem>>> = HashMap()

    override fun onChildrenLoaded(parentId: String, children: List<MediaBrowserCompat.MediaItem>) {

        (currentData[parentId] as MutableLiveData).postValue(children)


        val childrenArrayList = ArrayList(children)
        val listenersToNotify: Set<MediaBrowserSubscriber>? = subscribers[parentId]
        if (null != listenersToNotify) {
            for (listener in listenersToNotify) {
                listener.onChildrenLoaded(parentId, childrenArrayList)
            }
        }

    }

    @Deprecated("")
    @Synchronized
    fun registerMediaBrowserSubscriber(key: String, listener: MediaBrowserSubscriber?) {
        if (subscribers[key] == null) {
            subscribers[key] = HashSet()
        }
        subscribers[key]!!.add(listener!!)
    }

    fun subscribe(parentId : String) : LiveData<List<MediaBrowserCompat.MediaItem>> {
        if (!currentData.containsKey(parentId)) {
            currentData[parentId] = MutableLiveData<List<MediaBrowserCompat.MediaItem>>()
        }
        return currentData[parentId]!!
    }

    fun subscribeRoot(rootId : String) : LiveData<List<MediaBrowserCompat.MediaItem>> {
        currentData[rootId] = rootLiveData
        return rootLiveData
    }

    fun getRootLiveData() : LiveData<List<MediaBrowserCompat.MediaItem>> {
        return rootLiveData
    }
    @VisibleForTesting
    fun getMediaBrowserSubscribers() : Map<String, MutableSet<MediaBrowserSubscriber>> {
        return subscribers
    }

    override fun logTag(): String {
        return "SUBSCRIPTION_CALLBACK"
    }
}