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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class PlayerStateManagerTest {

    private val testScheduler : TestCoroutineScheduler = TestCoroutineScheduler()
    private val mainDispatcher : TestDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val ioDispatcher : TestDispatcher = StandardTestDispatcher(testScheduler)
    private val testScope : TestScope = TestScope()
    private val contentManager = mock<ContentManager>()
    private val iSavedStateRepository = mock<ISavedStateRepository>()
    private val mockPlayer = mock<Player>()
    private val initialSavedState = SavedState()
    private val contentManagerIsInitialisedFlow  = MutableStateFlow(false)
    private val saveStateFlow = MutableStateFlow(initialSavedState)

    private lateinit var playerStateManager: PlayerStateManager

    @Before
    fun setup() {
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

    @Test
    fun testCreateNewPlaylist() {
        val originalPlaylist: List<MediaItem> = emptyList()
   //     playerStateManager = PlayerStateManager(originalPlaylist.toMutableList())
       // Assert.assertEquals(originalPlaylist, playerStateManager!!.playlist)
        val newPlaylist: List<MediaItem?> = emptyList()
   //     playerStateManager!!.createNewPlaylist(newPlaylist)
   //     Assert.assertEquals(newPlaylist, playerStateManager!!.playlist)
    }

    companion object {
        private val MOCK_QUEUE_ITEM = mock<MediaItem>()
    }
}