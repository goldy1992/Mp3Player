package com.github.goldy1992.mp3player.client

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback

class MockMediaBrowserAdapter(mediaIdSubscriptionCallback: MediaIdSubscriptionCallback,
                              mySearchCallback: MySearchCallback ) :
        MediaBrowserAdapter(null, mediaIdSubscriptionCallback, mySearchCallback) {

    override fun disconnect() {
        // Do nothing.
    }

    override fun search(query: String?, extras: Bundle?) {
        // Do nothing.
    }

    override fun connect() {
        // Do nothing.
    }

    override fun subscribe(id: String) : LiveData<List<MediaItem>> {
        return MutableLiveData()
    }

    override fun subscribeToRoot() : LiveData<List<MediaItem>> {
        return MutableLiveData()
    }
}