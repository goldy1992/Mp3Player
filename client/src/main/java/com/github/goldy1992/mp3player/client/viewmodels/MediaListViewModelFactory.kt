package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.client.data.MediaSubscriptionRepository
import javax.inject.Inject

@FragmentScope
class MediaListViewModelFactory

    @Inject
    constructor(private val mediaSubscriptionRepository: MediaSubscriptionRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MediaListViewModel(mediaSubscriptionRepository) as T
    }
}