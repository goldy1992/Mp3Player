package com.github.goldy1992.mp3player.client.utils

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.views.fragments.SearchResultsFragment

class SearchResultFragmentAndroidTestImpl : SearchResultsFragment() {

    override fun onSearchResult(result : List<MediaBrowserCompat.MediaItem>) {
        super.onSearchResult(result)
        IdlingResources.idlingResource.decrement()
    }
}