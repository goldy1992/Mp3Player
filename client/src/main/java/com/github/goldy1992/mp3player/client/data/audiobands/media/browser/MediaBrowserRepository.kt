package com.github.goldy1992.mp3player.client.data.audiobands.media.browser

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import kotlinx.coroutines.flow.Flow

interface MediaBrowserRepository {

    fun currentSearchQuery() : Flow<String>

    fun onChildrenChanged() : Flow<OnChildrenChangedEventHolder>

    fun onCustomCommand() : Flow<SessionCommandEventHolder>

    fun onSearchResultsChanged() : Flow<OnSearchResultsChangedEventHolder>

    suspend fun getChildren(parentId : String,
                            @androidx.annotation.IntRange(from = 0) page : Int = 0,
                            @androidx.annotation.IntRange(from = 1) pageSize : Int = 20,
                            params : MediaLibraryService.LibraryParams = MediaLibraryService.LibraryParams.Builder().build()
    ) : List<MediaItem>

    suspend fun getLibraryRoot() : MediaItem

    suspend fun getSearchResults(query: String, page : Int = 0, pageSize : Int = 20) : List<MediaItem>

    suspend fun search(query: String, extras: Bundle)

    suspend fun subscribe(id : String)
}