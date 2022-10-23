package com.github.goldy1992.mp3player.client.viewmodels

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.CoroutineTestBase
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.data.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnChildrenChangedFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryScreenViewModelTest : CoroutineTestBase() {

    private val mediaBrowserAdapter = mock<MediaBrowserAdapter>()
    private val mediaControllerAdapter = mock<MediaControllerAdapter>()
    private val mediaController = mock<MediaController>()
    private val mediaControllerlf = Futures.immediateFuture(mediaController)
    private val isPlayingFlow = mock<IsPlayingFlow>()
    private val metadataFlow = mock<MetadataFlow>()
    private val onChildrenChangedFlow = mock<OnChildrenChangedFlow>()

    private lateinit var lsvm : LibraryScreenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        whenever(mediaControllerAdapter.mediaControllerFuture).thenReturn(mediaControllerlf)
        val f = MutableStateFlow<Boolean>(false)
        whenever(isPlayingFlow.flow()).thenReturn(f)

        val fM = MutableStateFlow<MediaMetadata>(MediaMetadata.EMPTY)
        whenever(metadataFlow.flow()).thenReturn(fM)

        val fC = MutableStateFlow<OnChildrenChangedEventHolder>(
            OnChildrenChangedEventHolder(mock<MediaBrowser>(),
            "", 0, MediaLibraryService.LibraryParams.Builder().build()))
        whenever(onChildrenChangedFlow.flow).thenReturn(fC)

    }

    @Test
    fun myTest() = testScope.runTest {
        whenever(mediaBrowserAdapter.getLibraryRoot()).thenReturn(MediaItem.EMPTY)
        lsvm  = LibraryScreenViewModel(
            mediaBrowserAdapter,
            mediaControllerAdapter,
            isPlayingFlow,
            metadataFlow,
            onChildrenChangedFlow,
            dispatcher
        )
        assertFalse(lsvm.isPlaying.state.value)
    }
}