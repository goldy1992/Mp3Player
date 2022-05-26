package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaIdSubscriptionCallback

    @Inject
    constructor() : MediaBrowserCompat.SubscriptionCallback(), LogTagger {

    private val rootLiveData = MutableLiveData<List<MediaBrowserCompat.MediaItem>>()

    private val currentData : MutableMap<String, LiveData<List<MediaBrowserCompat.MediaItem>>> = HashMap()

    override fun onChildrenLoaded(parentId: String, children: List<MediaBrowserCompat.MediaItem>) {
        if (currentData.containsKey(parentId)) {
            (currentData[parentId] as MutableLiveData).postValue(children)
        }
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

    override fun logTag(): String {
        return "SUBSCRIPTION_CALLBACK"
    }
}