package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.commons.MediaItemType
import javax.inject.Inject

class MainActivityViewModel

    @Inject
    constructor()
    : ViewModel() {

    val menuCategories = sortedMapOf<MediaItemType, MediaBrowserCompat.MediaItem>()
    var pagerItems = sortedMapOf<MediaItemType, MediaItemListFragment>()


}