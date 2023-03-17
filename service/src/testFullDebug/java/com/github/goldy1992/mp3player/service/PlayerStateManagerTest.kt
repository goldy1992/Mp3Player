package com.github.goldy1992.mp3player.service

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.service.data.ISavedStateRepository
import com.github.goldy1992.mp3player.service.data.SavedState
import com.github.goldy1992.mp3player.service.library.ContentManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class PlayerStateManagerTest {

    private val testScheduler : TestCoroutineScheduler = TestCoroutineScheduler()
    private val mainDispatcher : TestDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val ioDispatcher : TestDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val testScope : TestScope = TestScope()
    private val contentManager = mock<ContentManager>()
    private val iSavedStateRepository = mock<ISavedStateRepository>()
    private val mockPlayer = mock<Player>()
    private val initialSavedState = SavedState()
    private val contentManagerIsInitialisedFlow  = MutableStateFlow(false)
    private val saveStateFlow = MutableStateFlow(initialSavedState)
    private lateinit var playerStateManager: PlayerStateManager

    @Captor
    lateinit var argCaptor : ArgumentCaptor<SavedState>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        whenever(contentManager.isInitialised).thenReturn(contentManagerIsInitialisedFlow)
        whenever(iSavedStateRepository.getSavedState()).thenReturn(saveStateFlow)

        playerStateManager = PlayerStateManager(
            contentManager = contentManager,
            savedStateRepository = iSavedStateRepository,
            scope = testScope,
            mainDispatcher = mainDispatcher,
            ioDispatcher = ioDispatcher,
            player = mockPlayer)
    }

    /**
     * Test that
     * WHEN: emit true to the [ContentManager.isInitialised] flow
     * THEN: A valid saved state is loaded correctly into the player.
     */
    @Test
    fun testLoadPlayerStateValidState() = runTest {
        // set up a valid SavedState
        val playlistIds = listOf("1", "2", "3", "4", "5")
        val expectedPlaylist = playlistIds.map { MediaItemBuilder(it).build() }.toList()
        whenever(contentManager.getContentByIds(playlistIds)).thenReturn(expectedPlaylist)
        val currentTrackIndex = 1
        val currentTrackPosition = 999L
        val validSavedState = SavedState(
            playlist =  playlistIds,
            currentTrackIndex = currentTrackIndex,
            currentTrackPosition = currentTrackPosition
        )
        saveStateFlow.value = validSavedState


        // verify the loadPlayerState() function has not yet been invoked
        verify(mockPlayer, never()).prepare()
        assertFalse(contentManagerIsInitialisedFlow.value)

        // activate the initialisation coroutines
        contentManagerIsInitialisedFlow.value = true
        testScope.advanceUntilIdle()

        // assert loadPlayerState() was invoked and the player initialised as expected
        assertTrue(contentManagerIsInitialisedFlow.value)
        verify(mockPlayer, times(1)).prepare()
        verify(mockPlayer, times(1)).seekTo(currentTrackIndex, currentTrackPosition)
        verify(mockPlayer, times(1)).addMediaItems(expectedPlaylist)

        // cancel the test scope stop the infinitely running flow collectors
        testScope.cancel()
    }

    /**
     * Test that
     * WHEN: emit true to the [ContentManager.isInitialised] flow
     * THEN: A valid saved state is loaded correctly into the player.
     */
    @Test
    fun testLoadPlayerStateInvalidState() = runTest {
        // set up an invalid SavedState
        val playlistIds = listOf("1", "2", "3", "4", "5")
        val expectedPlaylist = playlistIds.map { MediaItemBuilder(it).build() }.toList()
        whenever(contentManager.getContentByIds(playlistIds)).thenReturn(expectedPlaylist)
        val currentTrackIndex = 1
        val currentTrackPosition = 999L
        val invalidSavedState = SavedState(
            playlist = emptyList(),
            currentTrackIndex = -1
        )
        saveStateFlow.value = invalidSavedState


        // verify the loadPlayerState() function has not yet been invoked
        verify(mockPlayer, never()).prepare()
        assertFalse(contentManagerIsInitialisedFlow.value)

        // activate the initialisation coroutines
        contentManagerIsInitialisedFlow.value = true
        testScope.advanceUntilIdle()

        // assert loadPlayerState() was invoked and the player is NOT as expected
        assertTrue(contentManagerIsInitialisedFlow.value)
        verify(mockPlayer, never()).prepare()
        verify(mockPlayer, never()).seekTo(currentTrackIndex, currentTrackPosition)
        verify(mockPlayer, never()).addMediaItems(expectedPlaylist)

        // cancel the test scope stop the infinitely running flow collectors
        testScope.cancel()
    }

    @Test
    fun testSaveState() = runTest {
        val expectedPlaylistIds = listOf("49", "54", "73")
        whenever(mockPlayer.mediaItemCount).thenReturn(expectedPlaylistIds.size)
        val playlist = expectedPlaylistIds.map { MediaItemBuilder(it).build() }.toList()
        playlist.forEachIndexed { index, mediaItem -> whenever(mockPlayer.getMediaItemAt(index)).thenReturn(mediaItem) }


        // activate the initialisation coroutines
        contentManagerIsInitialisedFlow.value = true
        testScope.advanceUntilIdle()
        playerStateManager.saveState()
        testScope.advanceUntilIdle()



        verify(iSavedStateRepository, times(1)).updateSavedState(capture(argCaptor))
        assertEquals(1,  argCaptor.allValues.size)
        val result = argCaptor.value
        assertEquals(expectedPlaylistIds, result.playlist)

        // cancel the test scope stop the infinitely running flow collectors
        testScope.cancel()

    }

    @Test
    fun testOnIsPlayingChanged() = runTest {
        val expectedPosition = 820L
        whenever(mockPlayer.currentPosition).thenReturn(expectedPosition)
        playerStateManager.onIsPlayingChanged(true)
        testScope.advanceUntilIdle()

        verify(iSavedStateRepository, times(1)).updateCurrentTrackPosition(expectedPosition)

        // cancel the test scope stop the infinitely running flow collectors
        testScope.cancel()
    }
}