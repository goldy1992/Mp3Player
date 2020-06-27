package com.github.goldy1992.mp3player.client.data

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import javax.inject.Inject

@FragmentScope
class MediaSubscriptionRepository

@Inject
constructor(val mediaBrowserAdapter: MediaBrowserAdapter,
            parentItemTypeId : String) : MediaBrowserResponseListener {

    var items : MutableLiveData<List<MediaBrowserCompat.MediaItem>> = MutableLiveData()

    init {
        mediaBrowserAdapter.registerListener(parentItemTypeId, this)
        mediaBrowserAdapter.subscribe(parentItemTypeId)
    }

    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaBrowserCompat.MediaItem>) {
        items.postValue(children)
    }
}