package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.session.SessionCommand
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnPlaybackPositionChangedEvent
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnQueueChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.data.sources.FakeMediaDataSource
import com.github.goldy1992.mp3player.client.models.ChildrenChangedEvent
import com.github.goldy1992.mp3player.client.models.CustomCommandEvent
import com.github.goldy1992.mp3player.client.models.PlaybackParametersEvent
import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.Queue
import com.github.goldy1992.mp3player.client.models.RepeatMode
import com.github.goldy1992.mp3player.client.models.SearchResultsChangedEvent
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.SearchResults
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Test class for [DefaultMediaRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@RunWith(RobolectricTestRunner::class)
class DefaultMediaRepositoryTest {

    private val fakeMediaDataSource : FakeMediaDataSource = FakeMediaDataSource()
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
            .setMediaMetadata(MediaMetadata.Builder().setIsPlayable(true).setIsBrowsable(false).build())
            .build()
        var result : Song? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.currentSong().collect {
                result = it
            }
        }
        fakeMediaDataSource.currentMediaItemState.value = testMediaItem
        assertEquals(testMediaId, result?.id)
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
    fun testOnChildrenChanged() = runTest {
        val expectedParentId = "expecPId"
        val expectedItemCount = 44

        var result : ChildrenChangedEvent? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.onChildrenChanged().collect {
                result = it
            }
        }
        fakeMediaDataSource.onChildrenChangedState.value = OnChildrenChangedEventHolder(parentId = expectedParentId,
        itemCount = expectedItemCount,
        params = getDefaultLibraryParams()
        )
        assertEquals(expectedParentId, result?.parentId)
        assertEquals(expectedItemCount, result?.itemCount)
        collectJob.cancel()
    }
    @Test
    fun testOnCustomCommand()  = runTest {
        val expectedCustomCommand = "CustomCommand"
        val customCommand = SessionCommandEventHolder(SessionCommand(expectedCustomCommand, Bundle()), Bundle())
        var result : CustomCommandEvent? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.onCustomCommand().collect {
                result = it
            }
        }
        fakeMediaDataSource.onCustomCommandState.value = customCommand
        assertEquals(expectedCustomCommand, result?.id)
        collectJob.cancel()
    }
    @Test
    fun testOnSearchResultsChanged()  = runTest {
        val expectedQuery = "expecPQueryy"
        val expectedItemCount = 44
        val expectedParams = Bundle()

        var result : SearchResultsChangedEvent? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.onSearchResultsChanged().collect {
                result = it
            }
        }

        fakeMediaDataSource.onSearchResultsChangedState.value = OnSearchResultsChangedEventHolder(expectedQuery, expectedItemCount, getDefaultLibraryParams())
        assertEquals(expectedQuery, result?.query)
        assertEquals(expectedItemCount, result?.itemCount)
        collectJob.cancel()
    }
    @Test
    fun testPlaybackParameters()  = runTest {
        val expectedSpeed = 66f
        val expectedPitch = 1.1f
        var result : PlaybackParametersEvent? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.playbackParameters().collect {
                result = it
            }
        }
        fakeMediaDataSource.playbackParametersState.value = PlaybackParameters(expectedSpeed, expectedPitch)
        assertEquals(expectedSpeed, result?.speed)
        assertEquals(expectedPitch, result?.pitch)
        collectJob.cancel()
    }
    @Test
    fun testPlaybackPosition()  = runTest {
        val expectedIsPlaying = false
        val expectedCurrentPosition = 111L
        val expectedSystemTime = 2234L

        var result : PlaybackPositionEvent? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.playbackPosition().collect {
                result = it
            }
        }
        fakeMediaDataSource.playbackPositionState.value = OnPlaybackPositionChangedEvent(isPlaying = expectedIsPlaying, currentPosition = expectedCurrentPosition, systemTime = expectedSystemTime)
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
        val expectedOnQueueChangedEventHolder = OnQueueChangedEventHolder(items = listOf(mediaItem1, mediaItem2), currentIndex = expectedCurrentIndex)

        var result : Queue? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.queue().collect {
                result = it
            }
        }
        fakeMediaDataSource.onQueueChangedEventHolder.value = expectedOnQueueChangedEventHolder

        assertEquals(expectedCurrentIndex, result?.currentIndex)
        assertEquals(mediaItem1Id, result?.items?.get(0)?.id)
        assertEquals(mediaItem2Id, result?.items?.get(1)?.id)
        collectJob.cancel()
    }
    @Test
    fun testRepeatMode()  = runTest {
        val expectedRepeatMode = RepeatMode.ALL

        var result : RepeatMode? = null
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            defaultMediaRepository.repeatMode().collect {
                result = it
            }
        }
        fakeMediaDataSource.repeatModeState.value = REPEAT_MODE_ALL
        assertEquals(expectedRepeatMode, result)
        collectJob.cancel()
    }
    @Test
    fun testChangePlaybackSpeed() = runTest {
        val expectedSpeed = 678f
        defaultMediaRepository.changePlaybackSpeed(expectedSpeed)
        assertEquals(expectedSpeed, fakeMediaDataSource.playbackSpeed)

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
        val result : Root = defaultMediaRepository.getLibraryRoot()
        assertEquals(expectedRootItemId, result.id)
    }
    @Test
    fun testGetSearchResults() = runTest {
        val mediaItem1Id = "id1"
        val mediaItem1 = MediaItemBuilder(mediaItem1Id).setMediaItemType(MediaItemType.SONG).build()
        val mediaItem2Id = "id2"
        val mediaItem2 = MediaItemBuilder(mediaItem2Id).setMediaItemType(MediaItemType.SONG).build()
        val expectedSearchResult = listOf(mediaItem1, mediaItem2)
        val query = "query"
        fakeMediaDataSource.searchResultsState = expectedSearchResult
        val result : SearchResults = defaultMediaRepository.getSearchResults(query, 4, 4)
        assertEquals(2, result.resultsMap.size)
        assertEquals(mediaItem1Id, result.getResult(0).value.id)
        assertEquals(query, result.getResult(0).id)
        assertEquals(mediaItem2Id, result.getResult(1).value.id)
        assertEquals(query, result.getResult(1).id)

    }
    @Test
    fun testPause()  = runTest {
        assertEquals(0, fakeMediaDataSource.pauseInvocations.get())
        defaultMediaRepository.pause()
        assertEquals(1, fakeMediaDataSource.pauseInvocations.get())

    }

    @Test
    fun testPlay() = runTest {
        assertEquals(0, fakeMediaDataSource.playInvocations.get())
        defaultMediaRepository.play()
        assertEquals(1, fakeMediaDataSource.playInvocations.get())
    }

    @Test
    fun testPlayFromSongList() = runTest {
        val expectedIndex = 4
        val song1 = Song("song1")
        val song2 = Song("song2")
        val songList = listOf(song1, song2)
        val expectedPlaylistId = "sdfdsfh"
        val playlist = Playlist(id=expectedPlaylistId, songs = songList)


        defaultMediaRepository.playPlaylist(playlist, expectedIndex)//, MediaMetadata.EMPTY)
        val mediaItems = fakeMediaDataSource.playlistItems
        assertEquals(2, mediaItems?.size)
        assertEquals(expectedIndex, fakeMediaDataSource.playlistItemIndex)
        assertEquals(expectedPlaylistId, fakeMediaDataSource.playlistId)


    }
    @Test
    fun testPlayFromUri() = runTest {
        val uri = Uri.EMPTY
        val extras = Bundle()
        defaultMediaRepository.playFromUri(uri, extras)
        assertEquals(uri, fakeMediaDataSource.uriToPlayFrom)
    }
    @Test
    fun testPrepareFromMediaId() = runTest {
        val expectedMediaId= "fsdf098-saLKJL"
        defaultMediaRepository.prepareFromId(expectedMediaId)
        assertEquals(expectedMediaId, fakeMediaDataSource.idToPrepare)
    }

    @Test
    fun testSearch() = runTest {
        val searchQuery = "queryyy"
        val extras = Bundle()
        defaultMediaRepository.search(searchQuery, extras)
        assertEquals(searchQuery, fakeMediaDataSource.searchQuery)
    }

    @Test
    fun testSeekTo() = runTest {
        val expectedSeekTo = 99L
        defaultMediaRepository.seekTo(expectedSeekTo)
        assertEquals(expectedSeekTo, fakeMediaDataSource.seekToValue)
    }

    @Test
    fun testSetRepeatMode()  = runTest {
        val initialRepeatMode = RepeatMode.ALL
        val expectedRepeatMode = Player.REPEAT_MODE_ALL
        defaultMediaRepository.setRepeatMode(initialRepeatMode)
        assertEquals(expectedRepeatMode, fakeMediaDataSource.repeatMode)
    }

    @Test
    fun testSetShuffleMode() = runTest {
        val expectedShuffleModeEnabled = true
        assertFalse(fakeMediaDataSource.shuffleEnabled)
        defaultMediaRepository.setShuffleMode(expectedShuffleModeEnabled)
        assertTrue(fakeMediaDataSource.shuffleEnabled)
    }

    @Test
    fun testSkipToNext()  = runTest {
        assertEquals(0, fakeMediaDataSource.skipToNextInvocations.get())
        defaultMediaRepository.skipToNext()
        assertEquals(1, fakeMediaDataSource.skipToNextInvocations.get())
    }

    @Test
    fun testSkipToPrevious() = runTest {
        assertEquals(0, fakeMediaDataSource.skipToPreviousInvocations.get())
        defaultMediaRepository.skipToPrevious()
        assertEquals(1, fakeMediaDataSource.skipToPreviousInvocations.get())
    }

    @Test
    fun testStop() = runTest {
        assertEquals(0, fakeMediaDataSource.stopInvocations.get())
        defaultMediaRepository.stop()
        assertEquals(1, fakeMediaDataSource.stopInvocations.get())
    }

    @Test
    fun testSubscribe() = runTest {
        val expected = "mediaId"
        assertNull(fakeMediaDataSource.subscribeId)
        defaultMediaRepository.subscribe(expected)
        assertEquals(expected, fakeMediaDataSource.subscribeId)
    }


}