package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.MediaBrowserSubscriber

abstract class MediaListViewModel
    : ViewModel(), MediaBrowserSubscriber {

    var items : MutableLiveData<List<MediaItem>> =  MutableLiveData()

    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaItem>) {
    /* TODO: only update livedata if its value has changed, for circumstanves such as screen rotation
        it is unlikely to need to be updated.
     */
        items.postValue(children)
    }

}