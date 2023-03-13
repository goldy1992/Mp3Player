package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.RepeatMode
import androidx.media3.session.SessionCommand
import com.github.goldy1992.mp3player.client.data.sources.FakeMediaDataSource
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Test class for [DefaultMediaRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultMediaRepositoryTest {

    private val fakeMediaDataSource : FakeMediaDataSource = spy(FakeMediaDataSource())
    private val defaultMediaRepository : DefaultMediaRepository = DefaultMediaRepository(fakeMediaDataSource)

    @Before
    fun setup() {

    }


    @Test
    fun testAudioData() = runTest {
        val testValue = 5
        var result : AudioSample? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.audioData().collect {
                result = it
            }
        }
        fakeMediaDataSource.audioDataState.value = AudioSample(channelCount = testValue)
        assertEquals(testValue, result?.channelCount)
        collectJob.cancel()
    }

    @Test
    fun testCurrentMediaItem() = runTest {
        val testMediaId = "testMediaId"
        val testMediaItem = MediaItem.Builder()
            .setMediaId(testMediaId)
            .build()
        var result : MediaItem? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.currentMediaItem().collect {
                result = it
            }
        }
        fakeMediaDataSource.currentMediaItemState.value = testMediaItem
        assertEquals(testMediaId, result?.mediaId)
        collectJob.cancel()
    }

    @Test
    fun testCurrentSearchQuery() = runTest {
        val expectedQuery = "newQuery"
        var result : String? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.currentSearchQuery().collect {
                result = it
            }
        }
        fakeMediaDataSource.currentSearchQueryState.value = expectedQuery
        assertEquals(expectedQuery, result)
        collectJob.cancel()
    }

    @Test
    fun testIsPlaying() = runTest {
        val expectedIsPlaying = true
        var result : Boolean? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.isPlaying().collect {
                result = it
            }
        }
        fakeMediaDataSource.isPlayingState.value = expectedIsPlaying
        assertEquals(expectedIsPlaying, result)
        collectJob.cancel()
    }

    @Test
    fun testIsShuffleModeEnabled() = runTest {
        val expectedShuffleModeEnabled = false
        var result : Boolean? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.isShuffleModeEnabled().collect {
                result = it
            }
        }
        fakeMediaDataSource.isPlayingState.value = expectedShuffleModeEnabled
        assertEquals(expectedShuffleModeEnabled, result)
        collectJob.cancel()
    }

    @Test
    fun testMetadata() = runTest {
        val testArtist = "myArtist"
        val metadata = MediaMetadata.Builder()
            .setArtist(testArtist)
            .build()
        var result : MediaMetadata? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.metadata().collect {
                result = it
            }
        }
        fakeMediaDataSource.metadataState.value = metadata
        assertEquals(testArtist, result?.artist)
        collectJob.cancel()
    }
    @Test
    fun testOnChildrenChanged() = runTest {
        val expectedParentId = "expecPId"
        val expectedItemCount = 44
        val expectedParams = getDefaultLibraryParams()
        val expectedOnChildrenChangedEventHolder = OnChildrenChangedEventHolder(
                                parentId = expectedParentId,
                                itemCount = expectedItemCount,
                                params = expectedParams)
        var result : OnChildrenChangedEventHolder? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.onChildrenChanged().collect {
                result = it
            }
        }
        fakeMediaDataSource.onChildrenChangedState.value = expectedOnChildrenChangedEventHolder
        assertEquals(expectedParentId, result?.parentId)
        assertEquals(expectedItemCount, result?.itemCount)
        assertEquals(expectedParams, result?.params)
        collectJob.cancel()
    }
    @Test
    fun testOnCustomCommand()  = runTest {
        val expectedCommandCode = SessionCommand.COMMAND_CODE_LIBRARY_SEARCH
        val expectedSessionCommand = SessionCommand(expectedCommandCode)
        val customCommand = SessionCommandEventHolder(expectedSessionCommand, Bundle())
        var result : SessionCommandEventHolder? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.onCustomCommand().collect {
                result = it
            }
        }
        fakeMediaDataSource.onCustomCommandState.value = customCommand
        assertEquals(expectedCommandCode, result?.command?.commandCode)
        collectJob.cancel()
    }
    @Test
    fun testOnSearchResultsChanged()  = runTest {
        val expectedQuery = "expecPQueryy"
        val expectedItemCount = 44
        val expectedParams = getDefaultLibraryParams()
        val expectedSearchResults =
            OnSearchResultsChangedEventHolder(
                query = expectedQuery,
                itemCount =  expectedItemCount,
                params = expectedParams
            )
        var result : OnSearchResultsChangedEventHolder? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.onSearchResultsChanged().collect {
                result = it
            }
        }
        fakeMediaDataSource.onSearchResultsChangedState.value = expectedSearchResults
        assertEquals(expectedQuery, result?.query)
        assertEquals(expectedItemCount, result?.itemCount)
        assertEquals(expectedParams, result?.params)
        collectJob.cancel()
    }
    @Test
    fun testPlaybackParameters()  = runTest {
        val expectedSpeed = 66f
        val expectedPlaybackParams = PlaybackParameters(expectedSpeed)

        var result : PlaybackParameters? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.playbackParameters().collect {
                result = it
            }
        }
        fakeMediaDataSource.playbackParametersState.value = expectedPlaybackParams
        assertEquals(expectedSpeed, result?.speed)
        collectJob.cancel()
    }
    @Test
    fun testPlaybackPosition()  = runTest {
        val expectedIsPlaying = false
        val expectedCurrentPosition = 111L
        val expectedSystemTime = 2234L
        val expectedPlaybackPosition = PlaybackPositionEvent(
            isPlaying = expectedIsPlaying,
            currentPosition = expectedCurrentPosition,
            systemTime = expectedSystemTime
        )

        var result : PlaybackPositionEvent? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.playbackPosition().collect {
                result = it
            }
        }
        fakeMediaDataSource.playbackPositionState.value = expectedPlaybackPosition
        assertEquals(expectedIsPlaying, result?.isPlaying)
        assertEquals(expectedCurrentPosition, result?.currentPosition)
        assertEquals(expectedSystemTime, result?.systemTime)
        collectJob.cancel()
    }
    @Test
    fun testPlaybackSpeed()  = runTest {
        val expectedPlaybackSpeed = 45f

        var result : Float? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.playbackSpeed().collect {
                result = it
            }
        }
        fakeMediaDataSource.playbackSpeedState.value = expectedPlaybackSpeed
        assertEquals(expectedPlaybackSpeed, result)
        collectJob.cancel()
    }

    @Test
    fun testQueue()  = runTest {
        val mediaItem1Id = "id1"
        val mediaItem1 = MediaItem.Builder().setMediaId(mediaItem1Id).build()
        val mediaItem2Id = "id2"
        val mediaItem2 = MediaItem.Builder().setMediaId(mediaItem2Id).build()
        val expectedCurrentIndex = 1
        val expectedQueueState = QueueState(items = listOf(mediaItem1, mediaItem2), currentIndex = expectedCurrentIndex)

        var result : QueueState? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.queue().collect {
                result = it
            }
        }
        fakeMediaDataSource.queueState.value = expectedQueueState

        assertEquals(expectedCurrentIndex, result?.currentIndex)
        assertEquals(mediaItem1Id, result?.items?.get(0)?.mediaId)
        assertEquals(mediaItem2Id, result?.items?.get(1)?.mediaId)
        collectJob.cancel()
    }
    @Test
    fun testRepeatMode()  = runTest {
        val expectedRepeatMode : @RepeatMode Int = REPEAT_MODE_ALL

        var result : @RepeatMode Int? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.repeatMode().collect {
                result = it
            }
        }
        fakeMediaDataSource.repeatModeState.value = expectedRepeatMode
        assertEquals(expectedRepeatMode, result)
        collectJob.cancel()
    }
    @Test
    fun testChangePlaybackSpeed() = runTest {
        val expectedSpeed = 678f
        defaultMediaRepository.changePlaybackSpeed(expectedSpeed)
        verify(fakeMediaDataSource, times(1)).changePlaybackSpeed(expectedSpeed)

    }
    @Test
    fun testGetChildren() = runTest {
        val expectedRootItemId = "rootId"
        val expectedMediaItem = MediaItem.Builder().setMediaId(expectedRootItemId).build()
        fakeMediaDataSource.getChildrenValue = listOf(expectedMediaItem)
        val result : List<MediaItem> = fakeMediaDataSource.getChildren("id", 2, 1, getDefaultLibraryParams())
        assertEquals(1, result.size)
        assertEquals(expectedRootItemId, result[0].mediaId)
    }
    @Test
    fun testGetLibraryRoot() = runTest {
        val expectedRootItemId = "rootId"
        val expectedMediaItem = MediaItem.Builder().setMediaId(expectedRootItemId).build()
        fakeMediaDataSource.libraryRootState = expectedMediaItem
        val result : MediaItem = defaultMediaRepository.getLibraryRoot()
        assertEquals(expectedRootItemId, result.mediaId)
    }
    @Test
    fun testGetSearchResults() = runTest {
        val mediaItem1Id = "id1"
        val mediaItem1 = MediaItem.Builder().setMediaId(mediaItem1Id).build()
        val mediaItem2Id = "id2"
        val mediaItem2 = MediaItem.Builder().setMediaId(mediaItem2Id).build()
        val expectedSearchResult = listOf(mediaItem1, mediaItem2)

        fakeMediaDataSource.searchResultsState = expectedSearchResult
        val result = defaultMediaRepository.getSearchResults("id", 4, 4)
        assertEquals(2, result.size)
        assertEquals(mediaItem1Id, result[0].mediaId)
        assertEquals(mediaItem2Id, result[1].mediaId)

    }
    @Test
    fun testPause()  = runTest {
        defaultMediaRepository.pause()
        verify(fakeMediaDataSource, times(1)).pause()
    }

    @Test
    fun testPlay() = runTest {
        defaultMediaRepository.play()
        verify(fakeMediaDataSource, times(1)).play()
    }

    @Test
    fun testPlayFromSongList() = runTest {
        val expectedIndex = 4
        val songList = emptyList<MediaItem>()
        defaultMediaRepository.playFromPlaylist(songList,expectedIndex, MediaMetadata.EMPTY)
        verify(fakeMediaDataSource, times(1)).playFromPlaylist(songList,expectedIndex, MediaMetadata.EMPTY)
    }
    @Test
    fun testPlayFromUri() = runTest {
        val uri = Uri.EMPTY
        val extras = Bundle()
        defaultMediaRepository.playFromUri(uri, extras)
        verify(fakeMediaDataSource, times(1)).playFromUri(uri, extras)
    }
    @Test
    fun testPrepareFromMediaId() = runTest {
        val expectedMediaItem = MediaItem.EMPTY
        defaultMediaRepository.prepareFromMediaId(expectedMediaItem)
        verify(fakeMediaDataSource, times(1)).prepareFromMediaId(expectedMediaItem)

    }

    @Test
    fun testSearch() = runTest {
        val searchQuery = "queryyy"
        val extras = Bundle()
        defaultMediaRepository.search(searchQuery, extras)
        verify(fakeMediaDataSource, times(1)).search(searchQuery, extras)
    }

    @Test
    fun testSeekTo() = runTest {
        val expectedSeekTo = 99L
        defaultMediaRepository.seekTo(expectedSeekTo)
        verify(fakeMediaDataSource, times(1)).seekTo(expectedSeekTo)
    }

    @Test
    fun testSetRepeatMode()  = runTest {
        val expectedRepeatMode : @RepeatMode Int = Player.REPEAT_MODE_ALL
        defaultMediaRepository.setRepeatMode(expectedRepeatMode)
        verify(fakeMediaDataSource, times(1)).setRepeatMode(expectedRepeatMode)
    }

    @Test
    fun testSetShuffleMode() = runTest {
        val expectedShuffleModeEnabled = false
        defaultMediaRepository.setShuffleMode(expectedShuffleModeEnabled)
        verify(fakeMediaDataSource, times(1)).setShuffleMode(expectedShuffleModeEnabled)
    }

    @Test
    fun testSkipToNext()  = runTest {
        defaultMediaRepository.skipToNext()
        verify(fakeMediaDataSource, times(1)).skipToNext()
    }

    @Test
    fun testSkipToPrevious() = runTest {
        defaultMediaRepository.skipToPrevious()
        verify(fakeMediaDataSource, times(1)).skipToPrevious()
    }

    @Test
    fun testStop() = runTest {
        defaultMediaRepository.stop()
        verify(fakeMediaDataSource, times(1)).stop()
    }

    @Test
    fun testSubscribe() = runTest {
        val expected = "mediaId"
        defaultMediaRepository.subscribe(expected)
        verify(fakeMediaDataSource, times(1)).subscribe(expected)
    }


}