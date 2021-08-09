package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

/**
 * Test class for [NowPlayingScreen].
 */
class NowPlayingScreenTest {

    private val mockMediaController = mock<MediaControllerAdapter>()

    private val mockNavController : NavController = mock<NavController>()

    @get:Rule
    val composeTestRule = createComposeRule()

    private val metadataLiveData = MutableLiveData<MediaMetadataCompat>()

    private val queueLiveData = MutableLiveData<MutableList<MediaSessionCompat.QueueItem>>()

    private lateinit var context : Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        whenever(mockMediaController.queue).thenReturn(queueLiveData)
        whenever(mockMediaController.metadata).thenReturn(metadataLiveData)
        whenever(mockMediaController.isPlaying).thenReturn(MutableLiveData(true))
        whenever(mockMediaController.playbackSpeed).thenReturn(MutableLiveData(1.0f))
        whenever(mockMediaController.shuffleMode).thenReturn(MutableLiveData(PlaybackStateCompat.SHUFFLE_MODE_ALL))
        whenever(mockMediaController.repeatMode).thenReturn(MutableLiveData(PlaybackStateCompat.REPEAT_MODE_ALL))
        whenever(mockMediaController.playbackState).thenReturn(MutableLiveData(PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_PLAYING, 0L, 1.0f)
            .build()))

        val queue = mutableListOf<MediaSessionCompat.QueueItem>(
            createMockQueueItem("title 1"),
            createMockQueueItem("title 2"),
            createMockQueueItem("title 3")
        )
        queueLiveData.postValue(queue)
    }

    @ExperimentalPagerApi
    @InternalCoroutinesApi
    @Test
    fun testDisplay() {
        val title = "Title"
        val artist = "Artist"
        metadataLiveData.postValue(
            MediaMetadataCompat.Builder()
                .putText(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .build()
        )


        composeTestRule.setContent {
            NowPlayingScreen(
                navController = mockNavController,
                mediaController = mockMediaController
            )
        }

        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText(title)
                .assertExists()
                .assertIsDisplayed()
            composeTestRule.onNodeWithText(artist)
                .assertExists()
                .assertIsDisplayed()

        }
    }

    @ExperimentalTestApi
    @ExperimentalPagerApi
    @InternalCoroutinesApi
    @Test
    fun testQueuePositionSwipeRightWhenNotAtBeginning() {
        val positionNotAtBeginning = 1
        runBlocking {
            runPagerSwipeTest(positionNotAtBeginning, false)
            verify(mockMediaController, never()).skipToNext()
            verify(mockMediaController, times(1)).skipToPrevious()
            verify(mockMediaController, times(1)).seekTo(0)
        }
    }

    @ExperimentalTestApi
    @ExperimentalPagerApi
    @InternalCoroutinesApi
    @Test
    fun testQueuePositionSwipeLeftWhenNotAtEnd() {
        val positionNotAtEnd = 1
        runBlocking {
            runPagerSwipeTest(positionNotAtEnd, swipeLeft = true)
            verify(mockMediaController, times(1)).skipToNext()
            verify(mockMediaController, never()).skipToPrevious()
            verify(mockMediaController, never()).seekTo(0)
        }
    }

    @ExperimentalPagerApi
    private fun runPagerSwipeTest(initialPosition : Int, swipeLeft : Boolean ) {
        whenever(mockMediaController.calculateCurrentQueuePosition())
            .thenReturn(initialPosition)
        metadataLiveData.postValue(MediaMetadataCompat.Builder()
            .putText(MediaMetadataCompat.METADATA_KEY_TITLE, "Title")
            .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artist")
            .build())


        composeTestRule.setContent {
            val pagerState = rememberPagerState(pageCount = queueLiveData.value!!.size, initialPage = initialPosition)
            ViewPager(mediaController = mockMediaController,
                pagerState = pagerState)
        }


        runBlocking {
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithContentDescription("viewPagerColumn").swipeAcrossCenter(
                MediumSwipeDistance,
                MediumVelocity,
                swipeLeft = swipeLeft
            )
            composeTestRule.awaitIdle()
        }
    }

    private fun createMockQueueItem(title : String) : MediaSessionCompat.QueueItem {
        val toReturn = mock<MediaSessionCompat.QueueItem>()
        val description = MediaDescriptionCompat.Builder()
            .setTitle(title)
            .build()
        whenever(toReturn.description).thenReturn(description)
        return toReturn
    }

}