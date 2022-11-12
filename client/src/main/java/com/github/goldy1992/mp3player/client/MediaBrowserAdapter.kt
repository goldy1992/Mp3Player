package com.github.goldy1992.mp3player.client

import android.os.Bundle
import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME_KEY
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils.isEmpty
import javax.inject.Inject

@ActivityRetainedScoped
open class MediaBrowserAdapter

    @Inject
    constructor(private val mediaBrowserLF : ListenableFuture<MediaBrowser>,
    private val scope: CoroutineScope,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher) : LogTagger {

    companion object {
        private fun getDefaultLibraryParams() : MediaLibraryService.LibraryParams {
            return MediaLibraryService.LibraryParams.Builder().build()
        }
    }

    open suspend fun search(query: String, extras: Bundle) {
        if (isEmpty(query)) {
            Log.w(logTag(), "Null or empty search query seen")
        }
        else {
            val params = MediaLibraryService.LibraryParams.Builder().setExtras(extras).build()
            mediaBrowserLF.await().search(query, params)
        }

    }

    open suspend fun getSearchResults(query: String, page : Int = 0, pageSize : Int = 20) : ImmutableList<MediaItem> {
        val result : LibraryResult<ImmutableList<MediaItem>> =
            mediaBrowserLF.await().getSearchResult(query, page, pageSize, getDefaultLibraryParams()).await()
        return result.value ?: ImmutableList.of()
    }

    /**
     * subscribes to a MediaItem via a libraryRequest. The id of the libraryRequest will be used for the parent
     * ID when communicating with the MediaPlaybackService.
     * @param id the id of the media item to be subscribed to
     */
    open suspend fun subscribe(id: String) {
        mediaBrowserLF.await().subscribe(id, getDefaultLibraryParams())
    }

    open suspend fun getLibraryRoot() : MediaItem {
        val args = Bundle()
        args.putString(PACKAGE_NAME_KEY, PACKAGE_NAME)
        val params = MediaLibraryService.LibraryParams.Builder().setExtras(args).build()
        val result = mediaBrowserLF.await().getLibraryRoot(params).await()
        return result.value ?: MediaItem.EMPTY
    }

    suspend fun getChildren(parentId : String,
                            @androidx.annotation.IntRange(from = 0) page : Int = 0,
                            @androidx.annotation.IntRange(from = 1) pageSize : Int = 20,
                            params : MediaLibraryService.LibraryParams = MediaLibraryService.LibraryParams.Builder().build()
    ) : List<MediaItem> {
        val children : LibraryResult<ImmutableList<MediaItem>> = mediaBrowserLF.await().getChildren(parentId, page, pageSize, params).await()
        return children.value?.toList() ?: emptyList()
    }

    override fun logTag(): String {
        return "MDIA_BRWSR_ADPTR"
    }

}