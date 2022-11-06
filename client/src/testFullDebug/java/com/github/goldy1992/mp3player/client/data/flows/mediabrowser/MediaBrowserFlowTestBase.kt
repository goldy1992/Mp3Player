package com.github.goldy1992.mp3player.client.data.flows.mediabrowser

import androidx.media3.session.MediaBrowser
import com.github.goldy1992.mp3player.client.AsyncMediaBrowserListener
import com.github.goldy1992.mp3player.client.CoroutineTestBase
import org.junit.Before
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.stubbing.Answer

abstract class MediaBrowserFlowTestBase : CoroutineTestBase() {

    protected val mediaBrowser = mock<MediaBrowser>()
    protected val asyncMediaBrowserListener  = mock<AsyncMediaBrowserListener>()
    var listener : MediaBrowser.Listener? = null

    @Before
    open fun setup() {
        val mockListeners = mock<MutableSet<MediaBrowser.Listener>>()

        whenever(asyncMediaBrowserListener.listeners).thenReturn(mockListeners)
        val answer = Answer {
            val argumentListener : MediaBrowser.Listener = it.getArgument(0, MediaBrowser.Listener::class.java) as MediaBrowser.Listener
            listener = argumentListener
            true
        }
        whenever(mockListeners.add(any())).thenAnswer(answer)
    }
}