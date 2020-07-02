package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.MediaBrowserSubscriber
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.FolderListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.SongListFragment
import com.github.goldy1992.mp3player.commons.*
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainActivityViewModel

    @ViewModelInject
    constructor()
    : ViewModel(), MediaBrowserSubscriber, LogTagger {

    val menuCategories : MutableLiveData<List<MediaItem>> = MutableLiveData(ArrayList())

    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaItem>) {
        val rootItemsOrdered = TreeSet(ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType)
        rootItemsOrdered.addAll(children)
        menuCategories.postValue(rootItemsOrdered.toList())
    }

    override fun logTag(): String {
        return "MAIN_ACTY_VIEW_MODEL"
    }


}