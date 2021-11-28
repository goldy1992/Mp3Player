package com.github.goldy1992.mp3player.client.ui.screens.main

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getEmptyMediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LargeMainScreenViewModel

    @Inject
    constructor(): ViewModel() {

    var currentNavigationItem : MutableLiveData<MediaItemType> = MutableLiveData(MediaItemType.SONGS)

    var mediaItemSelected : MutableLiveData<MediaItem> = MutableLiveData(getEmptyMediaItem())

    var mediaItemChildren : LiveData<List<MediaItem>> = MutableLiveData(emptyList())

}