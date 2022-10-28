package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.MediaTestUtils
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.client.data.flows.player.QueueFlow
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

    val metadataLiveData = MutableLiveData<MediaMetadata>()

    val queueFlow = mock<QueueFlow>()

    val searchResultsChangedFlow = mock<OnSearchResultsChangedFlow>()

    val metadataFlow = mock<MetadataFlow>()

    val searchResultsState = MutableStateFlow<List<MediaItem>>(emptyList())

    val isPlayingFlow = mock<IsPlayingFlow>()

    open fun setup() {

    }
    fun setup(scope : CoroutineScope,
            @MainDispatcher mainDispatcher: CoroutineDispatcher) {

        whenever(mockMediaBrowser.getLibraryRoot(any())).thenReturn(null)
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