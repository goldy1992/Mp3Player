package com.github.goldy1992.mp3player.client

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.search.SearchResultListener
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import javax.inject.Inject

open class MediaBrowserAdapter

    constructor(private val mediaBrowser: MediaBrowserCompat?,
                private val mySubscriptionCallback: MediaIdSubscriptionCallback,
                private val mySearchCallback: MySearchCallback) : LogTagger {

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
     * Connects to the media browser service
     */
    open fun connect() {
        mediaBrowser?.connect()
    }

    /**
     * subscribes to a MediaItem via a libraryRequest. The id of the libraryRequest will be used for the parent
     * ID when communicating with the MediaPlaybackService.
     * @param id the id of the media item to be subscribed to
     */
    open fun subscribe(id: String?) {
        mediaBrowser?.subscribe(id!!, mySubscriptionCallback)
    }

    open fun subscribeToRoot() {
        mediaBrowser?.subscribe(rootId, mySubscriptionCallback)
    }

    val mediaSessionToken: MediaSessionCompat.Token?
        get() = mediaBrowser?.sessionToken

    val rootId: String
        get() = mediaBrowser!!.root


    open fun registerRootListener(mediaBrowserResponseListener: MediaBrowserResponseListener) {
        mySubscriptionCallback.registerMediaBrowserResponseListener(rootId, mediaBrowserResponseListener)
    }

    open fun registerListener(parentId: String?, mediaBrowserResponseListener: MediaBrowserResponseListener) {
        mySubscriptionCallback.registerMediaBrowserResponseListener(parentId!!, mediaBrowserResponseListener)
    }

    fun registerSearchResultListener(searchResultListener: SearchResultListener?) {
        mySearchCallback.registerSearchResultListener(searchResultListener!!)
    }

    override fun logTag(): String {
        return "MDIA_BRWSR_ADPTR"
    }

}