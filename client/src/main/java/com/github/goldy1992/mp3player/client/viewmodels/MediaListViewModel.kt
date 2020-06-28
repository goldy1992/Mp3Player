package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener
import com.github.goldy1992.mp3player.commons.MediaItemType
import javax.inject.Inject

abstract class MediaListViewModel

    @Inject
    constructor(
                private val mediaBrowserAdapter: MediaBrowserAdapter,
                val mediaControllerAdapter: MediaControllerAdapter,
                 parentItemTypeId: String)
    : ViewModel(), MediaBrowserResponseListener, MyGenericItemTouchListener .ItemSelectedListener {

    init {
        mediaBrowserAdapter.registerListener(parentItemTypeId, this)
        mediaBrowserAdapter.subscribe(parentItemTypeId)
    }

    var items : MutableLiveData<List<MediaBrowserCompat.MediaItem>> =  MutableLiveData()

    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaBrowserCompat.MediaItem>) {
        items.postValue(children)
    }

}