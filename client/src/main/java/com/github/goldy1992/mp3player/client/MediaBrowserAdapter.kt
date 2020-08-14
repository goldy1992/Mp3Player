package com.github.goldy1992.mp3player.client

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.session.MediaSessionCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.search.SearchResultListener
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import com.github.goldy1992.mp3player.commons.LogTagger


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
        mediaBrowser?.search(query!!, extras, mySearchCallback)
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

    /**
     * subscribes to a MediaItem via a libraryRequest. The id of the libraryRequest will be used for the parent
     * ID when communicating with the MediaPlaybackService.
     * @param id the id of the media item to be subscribed to
     */
    open fun subscribe(id: String, mediaBrowserSubscriber: MediaBrowserSubscriber) {
        mySubscriptionCallback.registerMediaBrowserSubscriber(id, mediaBrowserSubscriber)
        mediaBrowser?.subscribe(id, mySubscriptionCallback)
    }

    @Deprecated("Use subscribeToRoot()")
    open fun subscribeToRoot(listener : MediaBrowserSubscriber) {
        mySubscriptionCallback.registerMediaBrowserSubscriber(rootId, listener)
        mediaBrowser?.subscribe(rootId, mySubscriptionCallback)
    }

    open fun subscribeToRoot() : LiveData<List<MediaItem>> {
        return mySubscriptionCallback.getRootLiveData()
    }

         /** Called when the component has successfully connected to the MediaBrowserService. */
    override fun onConnected() {
        mySubscriptionCallback.subscribeRoot(rootId)
        mediaBrowser?.subscribe(rootId, mySubscriptionCallback)
    }

    val mediaSessionToken: MediaSessionCompat.Token?
        get() = mediaBrowser?.sessionToken

    val rootId: String
        get() = mediaBrowser!!.root


    open fun registerRootListener(mediaBrowserSubscriber: MediaBrowserSubscriber) {
        mySubscriptionCallback.registerMediaBrowserSubscriber(rootId, mediaBrowserSubscriber)
    }

    open fun registerListener(parentId: String?, mediaBrowserSubscriber: MediaBrowserSubscriber) {
        mySubscriptionCallback.registerMediaBrowserSubscriber(parentId!!, mediaBrowserSubscriber)
    }

    fun registerSearchResultListener(searchResultListener: SearchResultListener?) {
        mySearchCallback.registerSearchResultListener(searchResultListener!!)
    }

    override fun logTag(): String {
        return "MDIA_BRWSR_ADPTR"
    }

}