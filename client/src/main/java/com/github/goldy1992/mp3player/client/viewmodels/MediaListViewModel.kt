package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.MediaBrowserSubscriber
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener

abstract class MediaListViewModel
    : ViewModel(), MediaBrowserSubscriber {

    var isSubscribed : Boolean = false

    var items : MutableLiveData<List<MediaBrowserCompat.MediaItem>> =  MutableLiveData()

    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaBrowserCompat.MediaItem>) {
    /* TODO: only update livedata if its value has changed, for circumstanves such as screen rotation
        it is unlikely to need to be updated.
     */
        isSubscribed = true
        items.postValue(children)
    }

}