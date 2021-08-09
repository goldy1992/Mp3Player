package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

abstract class MediaTestBase {

    val mockMediaBrowser : MediaBrowserAdapter = mock<MediaBrowserAdapter>()

    val mockMediaController : MediaControllerAdapter = mock<MediaControllerAdapter>()

    val mockNavController : NavController = mock<NavController>()

    lateinit var context : Context

    val metadataLiveData = MutableLiveData<MediaMetadataCompat>()

    val queueLiveData = MutableLiveData<MutableList<MediaSessionCompat.QueueItem>>()

    val searchResultsLiveData = MutableLiveData<List<MediaBrowserCompat.MediaItem>>()

    open fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        whenever(mockMediaBrowser.searchResults()).thenReturn(searchResultsLiveData)
        whenever(mockMediaController.queue).thenReturn(queueLiveData)
        whenever(mockMediaController.metadata).thenReturn(metadataLiveData)
        whenever(mockMediaController.isPlaying).thenReturn(MutableLiveData(true))
        whenever(mockMediaController.playbackSpeed).thenReturn(MutableLiveData(1.0f))
        whenever(mockMediaController.shuffleMode).thenReturn(MutableLiveData(PlaybackStateCompat.SHUFFLE_MODE_ALL))
        whenever(mockMediaController.repeatMode).thenReturn(MutableLiveData(PlaybackStateCompat.REPEAT_MODE_ALL))
        whenever(mockMediaController.playbackState).thenReturn(
            MutableLiveData(
                PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_PLAYING, 0L, 1.0f)
            .build())
        )

    }
}