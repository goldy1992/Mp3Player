package com.github.goldy1992.mp3player.client.ui

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.MediaTestUtils
import com.github.goldy1992.mp3player.client.data.audiobands.media.browser.MediaBrowserRepository
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.mockito.kotlin.mock

class TestMediaBrowserRepository() : MediaBrowserRepository {
    val currentSearchQuery = MutableStateFlow("value")
    override fun currentSearchQuery(): Flow<String> {
        return currentSearchQuery
    }

    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        TODO("Not yet implemented")
    }

    override fun onCustomCommand(): Flow<SessionCommandEventHolder> {
        TODO("Not yet implemented")
    }

    val onSearchResultsChangedValue = MutableStateFlow<OnSearchResultsChangedEventHolder>(
        OnSearchResultsChangedEventHolder(
            browser = mock<MediaBrowser>(),
            query = "",
            itemCount = 1,
            params = MediaTestUtils.getDefaultLibraryParams()
        )
    )
    override fun onSearchResultsChanged(): Flow<OnSearchResultsChangedEventHolder> {
        return onSearchResultsChangedValue
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getLibraryRoot(): MediaItem {
        TODO("Not yet implemented")
    }

    val currentSearchResults = MutableStateFlow<List<MediaItem>>(emptyList())
    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {
        return currentSearchResults.value
    }

    override suspend fun search(query: String, extras: Bundle) {
        currentSearchQuery.value = query
    }

    override suspend fun subscribe(id: String) {
        TODO("Not yet implemented")
    }
}