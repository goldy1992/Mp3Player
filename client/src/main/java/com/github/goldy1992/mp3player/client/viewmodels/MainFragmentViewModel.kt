package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.MediaBrowserSubscriber
import com.github.goldy1992.mp3player.commons.ComparatorUtils
import com.github.goldy1992.mp3player.commons.LogTagger
import java.util.*
import kotlin.collections.ArrayList

/**
 * [ViewModel] class for the [com.github.goldy1992.mp3player.client.views.fragments.MainFragment].
 */
class MainFragmentViewModel

    @ViewModelInject
    constructor()
    : ViewModel(), MediaBrowserSubscriber, LogTagger {

    init {
        Log.i(logTag(), "creating MainActivityViewModel")
    }

    var menuCategories : MutableLiveData<List<MediaItem>> = MutableLiveData(ArrayList())

    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaItem>) {
        val rootItemsOrdered = TreeSet(ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType)
        rootItemsOrdered.addAll(children)
        // TODO: compare new list and old list to see if need to update
        menuCategories.postValue(rootItemsOrdered.toList())
    }

    override fun logTag(): String {
        return "MAIN_ACTY_VIEW_MODEL"
    }
}