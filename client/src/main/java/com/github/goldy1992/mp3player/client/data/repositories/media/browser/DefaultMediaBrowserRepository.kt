package com.github.goldy1992.mp3player.client.data.repositories.media.browser

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.sources.MediaDataSource
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ActivityRetainedScoped
class DefaultMediaBrowserRepository
    @Inject
    constructor(
        private val mediaDataSource : MediaDataSource
    ) : MediaBrowserRepository, LogTagger {

    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return mediaDataSource.onChildrenChanged()
    }

    override fun onCustomCommand() : Flow<SessionCommandEventHolder> {
        return mediaDataSource.onCustomCommand()
    }

    override fun onSearchResultsChanged() : Flow<OnSearchResultsChangedEventHolder> {
        return mediaDataSource.onSearchResultsChanged()
    }



    override fun currentSearchQuery(): Flow<String> {
        return mediaDataSource.currentSearchQuery()
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        return mediaDataSource.getChildren(parentId, page, pageSize, params)
    }

    override suspend fun getLibraryRoot(): MediaItem {
        return mediaDataSource.getLibraryRoot()
    }
    override suspend fun getSearchResults(query: String, page: Int, pageSize: Int) : List<MediaItem> {
        return mediaDataSource.getSearchResults(query, page, pageSize)
    }

    override suspend fun search(query: String, extras: Bundle) {
        mediaDataSource.search(query, extras)
    }

    override suspend fun subscribe(id : String) {
        mediaDataSource.subscribe(id)
    }

    override fun logTag(): String {
        return "DefaultMediaBrowserRepo"
    }
}