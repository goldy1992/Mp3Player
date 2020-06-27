package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.client.data.MediaSubscriptionRepository
import javax.inject.Inject

@FragmentScope
class MediaListViewModel

    @Inject
    constructor(private val mediaSubscriptionRepository: MediaSubscriptionRepository)
    : ViewModel() {

    var items : LiveData<List<MediaBrowserCompat.MediaItem>> = mediaSubscriptionRepository.items

}