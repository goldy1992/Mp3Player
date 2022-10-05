package com.github.goldy1992.mp3player.client

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.concurrent.futures.await
import androidx.lifecycle.LiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils.isEmpty

open class MediaBrowserAdapter

    constructor(@ApplicationContext context: Context,
                sessionToken: SessionToken,
    private val scope: CoroutineScope,
    private val asyncMediaBrowserListener: AsyncMediaBrowserListener) : LogTagger, MediaBrowser.Listener {

    private var mediaBrowser : MediaBrowser? = null

    init {
        scope.launch {
            mediaBrowser = MediaBrowser.Builder(context, sessionToken).setListener(asyncMediaBrowserListener).buildAsync().await()

        }

    }

    companion object {
        private fun getDefaultLibraryParams() : MediaLibraryService.LibraryParams {
            return MediaLibraryService.LibraryParams.Builder().build()
        }
    }

    open fun search(query: String, extras: Bundle) {
        val params = MediaLibraryService.LibraryParams.Builder().setExtras(extras).build()
        scope.launch { mediaBrowser?.search(query, params) }
        if (isEmpty(query)) {
            Log.w(logTag(), "Null or empty search query seen")
        }
    }

    open suspend fun getSearchResults(query: String, page : Int = 0, pageSize : Int = 20) : ImmutableList<MediaItem> {
        val result : LibraryResult<ImmutableList<MediaItem>> =
            mediaBrowser?.getSearchResult(query, page, pageSize, getDefaultLibraryParams())?.await()
                ?:
            LibraryResult.ofItemList(emptyList(), getDefaultLibraryParams())
        return result.value ?: ImmutableList.of()
    }

    private val searchResultsChangedFlow : Flow<String> = callbackFlow {
        val messageListener = object : MediaBrowser.Listener {
            override fun onSearchResultChanged(
                browser: MediaBrowser,
                query: String,
                itemCount: Int,
                params: MediaLibraryService.LibraryParams?
            ) {
                super.onSearchResultChanged(browser, query, itemCount, params)
                trySend(query)
            }
        }
        asyncMediaBrowserListener.listeners.add(messageListener)
        awaitClose()
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )


//    open fun clearSearchResults() {
//        mySearchCallback.searchResults.postValue(emptyList())
//    }

    /**
     * subscribes to a MediaItem via a libraryRequest. The id of the libraryRequest will be used for the parent
     * ID when communicating with the MediaPlaybackService.
     * @param id the id of the media item to be subscribed to
     */
    open suspend fun subscribe(id: String) {
        scope.launch {
            mediaBrowser?.subscribe(id, getDefaultLibraryParams())?.await()
        }
    }

    open suspend fun getLibraryRoot() : MediaItem {
        val result = mediaBrowser?.getLibraryRoot(getDefaultLibraryParams())?.await()
        return result?.value ?: MediaItem.EMPTY
    }

    override fun logTag(): String {
        return "MDIA_BRWSR_ADPTR"
    }

}