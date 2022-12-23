package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.sources.FakeMediaDataSource
import com.github.goldy1992.mp3player.client.data.sources.MediaDataSource
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    fun testonChildrenChanged() {
  
    }

    fun testonCustomCommand() {

    }

    fun testonSearchResultsChanged() {

    }

    fun testplaybackParameters() {

    }

    fun testplaybackPosition() {

    }

    fun testplaybackSpeed() {

    }

    fun testqueue() {

    }

    fun testrepeatMode() {

    }

    fun testchangePlaybackSpeed(speed: Float) {

    }

    fun testgetChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ){

    }

    fun testgetLibraryRoot(){

    }

    fun testgetSearchResults(query: String, page: Int, pageSize: Int)  {
 
    }

    fun testpause() {

    }

    fun testplay() {

    }

    fun testplay(mediaItem: MediaItem) {

    }

    fun testplayFromSongList(itemIndex: Int, items: List<MediaItem>) {

    }

    fun testplayFromUri(uri: Uri?, extras: Bundle?) {

    }

    fun testprepareFromMediaId(mediaItem: MediaItem) {

    }

    fun testsearch(query: String, extras: Bundle) {

    }

    fun testseekTo(position: Long) {

    }

    fun testsetRepeatMode(repeatMode: Int) {

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