package com.github.goldy1992.mp3player.client

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import androidx.lifecycle.LiveData
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import com.github.goldy1992.mp3player.commons.LogTagger
import org.apache.commons.lang3.StringUtils.isEmpty

open class MediaBrowserAdapter

    constructor(private val mediaBrowser: MediaBrowserCompat?,
                private val mySubscriptionCallback: MediaIdSubscriptionCallback,
                private val mySearchCallback: MySearchCallback) : LogTagger, MediaBrowserConnectionListener {


    /**
     * Disconnects from the media browser service
     */
    open fun disconnect() {
        mediaBrowser?.disconnect()
    }

    open fun search(query: String?, extras: Bundle?) {
        if (isEmpty(query)) {
            Log.w(logTag(), "Null or empty search query seen")
        } else {
            mediaBrowser?.search(query!!, extras, mySearchCallback)
        }
    }

    open fun searchResults() : LiveData<List<MediaItem>> {
        return mySearchCallback.searchResults
    }

    open fun clearSearchResults() {
        mySearchCallback.searchResults.postValue(emptyList())
    }

    /**
     * @return True if the mediaBrowser is connected
     */
    open fun isConnected() : Boolean {
        return mediaBrowser != null && mediaBrowser.isConnected
    }
    /**
     * Connects to the media browser service
     */
    open fun connect() {
        if (!isConnected()) {
            mediaBrowser?.connect()
        }
    }

    /**
     * subscribes to a MediaItem via a libraryRequest. The id of the libraryRequest will be used for the parent
     * ID when communicating with the MediaPlaybackService.
     * @param id the id of the media item to be subscribed to
     */
    open fun subscribe(id: String) : LiveData<List<MediaItem>> {
        val toReturn = mySubscriptionCallback.subscribe(id)
        mediaBrowser?.subscribe(id, mySubscriptionCallback)
        return toReturn
    }

    open fun subscribeToRoot() : LiveData<List<MediaItem>> {
        return mySubscriptionCallback.getRootLiveData()
    }

    private val rootId: String
        get() = mediaBrowser?.root ?: ""


    override fun logTag(): String {
        return "MDIA_BRWSR_ADPTR"
    }

    override fun onConnected() {
        mySubscriptionCallback.subscribeRoot(rootId)
        mediaBrowser?.subscribe(rootId, mySubscriptionCallback)
    }

}