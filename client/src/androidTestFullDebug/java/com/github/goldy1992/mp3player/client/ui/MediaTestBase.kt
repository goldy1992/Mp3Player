package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.client.data.flows.player.QueueFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

abstract class MediaTestBase {

    val mockMediaBrowser : MediaBrowserAdapter = mock<MediaBrowserAdapter>()

    val mockMediaController : MediaControllerAdapter = mock<MediaControllerAdapter>()

    val mockNavController : NavController = mock<NavController>()

    lateinit var context : Context

    val metadataLiveData = MutableLiveData<MediaMetadata>()

    val queueFlow = mock<QueueFlow>()

    val searchResultsChangedFlow = mock<OnSearchResultsChangedFlow>()

    val metadataFlow = mock<MetadataFlow>()

    val searchResultsState = MutableStateFlow<List<MediaItem>>(emptyList())

    val isPlayingFlow = mock<IsPlayingFlow>()

    open fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
     //   whenever(mockMediaBrowser.searchResults()).thenReturn(searchResultsState)
        whenever(queueFlow.state).thenReturn(MutableStateFlow(emptyList()))
     //   whenever(metadataFlow.state).thenReturn(metadataLiveData)
        whenever(isPlayingFlow.state).thenReturn(MutableStateFlow(true))
//        whenever(mockMediaController.playbackSpeed).thenReturn(MutableLiveData(1.0f))
//        whenever(mockMediaController.shuffleMode).thenReturn(MutableLiveData(PlaybackStateCompat.SHUFFLE_MODE_ALL))
//        whenever(mockMediaController.repeatMode).thenReturn(MutableLiveData(PlaybackStateCompat.REPEAT_MODE_ALL))
//        whenever(mockMediaController.playbackState).thenReturn(
//            MutableLiveData(
//                PlaybackStateCompat.Builder()
//            .setState(PlaybackStateCompat.STATE_PLAYING, 0L, 1.0f)
//            .build())
//        )

    }
}