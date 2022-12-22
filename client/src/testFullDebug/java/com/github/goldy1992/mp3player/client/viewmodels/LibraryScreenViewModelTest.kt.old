package com.github.goldy1992.mp3player.client.viewmodels

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.CoroutineTestBase
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.flows.mediabrowser.OnChildrenChangedFlow
import com.github.goldy1992.mp3player.client.ui.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.ui.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreenViewModel
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
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
    private val isPlayingProducerFlow = MutableSharedFlow<Boolean>(1)

    private val metadataProducerFlow = MutableStateFlow(MediaMetadata.EMPTY)
    private val onChildrenChangedProducerFlow = MutableStateFlow(
        OnChildrenChangedEventHolder(mock<MediaBrowser>(),
            "", 0, MediaLibraryService.LibraryParams.Builder().build())
    )


    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        whenever(mediaControllerAdapter.mediaControllerFuture).thenReturn(mediaControllerlf)
        whenever(mediaController.mediaMetadata).thenReturn(MediaMetadata.EMPTY)
        whenever(mediaController.isPlaying).thenReturn(false)
        whenever(isPlayingFlow.flow()).thenReturn(isPlayingProducerFlow)
        whenever(metadataFlow.flow()).thenReturn(metadataProducerFlow)
        whenever(onChildrenChangedFlow.flow).thenReturn(onChildrenChangedProducerFlow)

    }

    @Test
    fun testIsPlaying() = testScope.runTest {
        whenever(mediaBrowserAdapter.getChildren(any(), any(), any(), any())).thenReturn(emptyList())
        whenever(mediaBrowserAdapter.getLibraryRoot()).thenReturn(MediaItem.EMPTY)
        lsvm  = LibraryScreenViewModel(
            mediaBrowserAdapter,
            mediaControllerAdapter,
            isPlayingFlow,
            metadataFlow,
            onChildrenChangedFlow,
            UnconfinedTestDispatcher(testScheduler) // use unconfined test dispatcher with view model to ensure collect coroutines to not block the test dispatcher
        )
        assertFalse(lsvm.isPlaying.state.value)

        isPlayingProducerFlow.emit(true)
        testScope.advanceUntilIdle()
        assertTrue(lsvm.isPlaying.state.value)
    }
}