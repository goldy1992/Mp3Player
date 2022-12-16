package com.github.goldy1992.mp3player.client.data.audiobands.media.browser

import android.os.Bundle
import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.ui.flows.mediabrowser.OnChildrenChangedFlow
import com.github.goldy1992.mp3player.client.ui.flows.mediabrowser.OnCustomCommandFlow
import com.github.goldy1992.mp3player.client.ui.flows.mediabrowser.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

@ActivityRetainedScoped
class DefaultMediaBrowserRepository
    @Inject
    constructor(
        private val mediaBrowserLF : ListenableFuture<MediaBrowser>,
        private val onChildrenChangedFlow: OnChildrenChangedFlow,
        private val onCustomCommandFlow: OnCustomCommandFlow,
        private val onSearchResultsChangedFlow: OnSearchResultsChangedFlow
    ) : MediaBrowserRepository, LogTagger {

    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return onChildrenChangedFlow.flow
    }

    override fun onCustomCommand() : Flow<SessionCommandEventHolder> {
        return onCustomCommandFlow.flow
    }

    override fun onSearchResultsChanged() : Flow<OnSearchResultsChangedEventHolder> {
        return onSearchResultsChangedFlow.flow
    }

    private val _currentSearchQuery = MutableStateFlow("")
    private val currentSearchQuery : StateFlow<String> = _currentSearchQuery

    override fun currentSearchQuery(): Flow<String> {
        return currentSearchQuery
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        val children : LibraryResult<ImmutableList<MediaItem>> = mediaBrowserLF.await().getChildren(parentId, page, pageSize, params).await()
        return children.value?.toList() ?: emptyList()
    }

    override suspend fun getLibraryRoot(): MediaItem {
        val args = Bundle()
        args.putString(Constants.PACKAGE_NAME_KEY, Constants.PACKAGE_NAME)
        val params = MediaLibraryService.LibraryParams.Builder().setExtras(args).build()
        val result = mediaBrowserLF.await().getLibraryRoot(params).await()
        return result.value ?: MediaItem.EMPTY
    }
    override suspend fun getSearchResults(query: String, page: Int, pageSize: Int) : List<MediaItem> {
        val browser = mediaBrowserLF.await()
        return browser.getSearchResult(query, page, pageSize, getDefaultLibraryParams()).await().value ?: emptyList()
    }

    override suspend fun search(query: String, extras: Bundle) {
        _currentSearchQuery.value = query
        if (StringUtils.isEmpty(query)) {
            Log.w(logTag(), "Null or empty search query seen")
        }
        else {
            val params = MediaLibraryService.LibraryParams.Builder().setExtras(extras).build()
            mediaBrowserLF.await().search(query, params)
        }
    }

    override suspend fun subscribe(id : String) {
        mediaBrowserLF.await().subscribe(id, getDefaultLibraryParams())
    }

    override fun logTag(): String {
        return "DefaultMediaBrowserRepo"
    }
}