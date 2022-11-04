package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.MediaLibraryService
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.client.data.flows.player.QueueFlow
import com.github.goldy1992.mp3player.client.MediaTestUtils.createTestMediaItem
import com.github.goldy1992.mp3player.client.data.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.data.flows.player.PlaybackPositionFlow
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

abstract class MediaTestBase {

    lateinit var mediaBrowserAdapter : MediaBrowserAdapter
    lateinit var mediaControllerAdapter : MediaControllerAdapter

    val mockMediaController = mock<MediaController>()
    val mediaControllerListenableFuture = Futures.immediateFuture(mockMediaController)

    val mockMediaBrowser = mock<MediaBrowser>()
    val mediaBrowserListenableFuture = Futures.immediateFuture(mockMediaBrowser)



    val mockNavController : NavController = mock<NavController>()

    open lateinit var context : Context


    val queueFlow = mock<QueueFlow>()

    val metadataFlowObj = mock<MetadataFlow>()
    val metadataFlow = MutableStateFlow(MediaMetadata.EMPTY)

    val searchResultsState = MutableStateFlow<List<MediaItem>>(emptyList())

    val isPlayingFlowObj = mock<IsPlayingFlow>()
    val isPlayingFlow = MutableStateFlow(false)

    val playbackPositionFlowObj = mock<PlaybackPositionFlow>()
    val playbackPositionFlow = MutableStateFlow(PlaybackPositionEvent.DEFAULT)

    open fun setup() {

    }
    fun setup(scope : CoroutineScope,
            @MainDispatcher mainDispatcher: CoroutineDispatcher) {

        whenever(mockMediaController.mediaMetadata).thenReturn(MediaMetadata.EMPTY)
        whenever(isPlayingFlowObj.flow()).thenReturn(isPlayingFlow)
        whenever(metadataFlowObj.flow()).thenReturn(metadataFlow)
        whenever(playbackPositionFlowObj.flow()).thenReturn(playbackPositionFlow)
        whenever(mockMediaBrowser.getLibraryRoot(any()))
            .thenReturn(
                Futures
                    .immediateFuture(LibraryResult
                        .ofItem(createTestMediaItem("id"),
                                MediaLibraryService.LibraryParams.Builder().build()))
            )
        mediaBrowserAdapter = MediaBrowserAdapter(
            mediaBrowserLF = mediaBrowserListenableFuture,
            scope = scope,
            mainDispatcher = mainDispatcher)
        mediaControllerAdapter = MediaControllerAdapter(
            mediaControllerFuture = mediaControllerListenableFuture,
            scope = scope,
            mainDispatcher = mainDispatcher)

    }
}