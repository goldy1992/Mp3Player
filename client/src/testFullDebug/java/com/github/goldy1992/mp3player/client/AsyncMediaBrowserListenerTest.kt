package com.github.goldy1992.mp3player.client

import android.os.Bundle
import androidx.media3.session.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class AsyncMediaBrowserListenerTest {

    private val asyncMediaBrowserListener = AsyncMediaBrowserListener()
    private val mockMediaBrowserListener = mock<MediaBrowser.Listener>()
    private val browser = mock<MediaBrowser>()
    private val mediaController = mock<MediaController>()
    private val params = MediaLibraryService.LibraryParams.Builder().build()

    @Before
    fun setup() {
        asyncMediaBrowserListener.listeners.add(mockMediaBrowserListener)
    }

    @Test
    fun testOnChildrenChanged() {
        val parentId = "parentId"
        val itemCount = 7
        asyncMediaBrowserListener.onChildrenChanged(browser, parentId, itemCount, params)
        verify(mockMediaBrowserListener, times(1)).onChildrenChanged(browser, parentId, itemCount, params)
    }

    @Test
    fun testOnSearchResultsChanged() {
        val query = "query"
        val itemCount = 7
        asyncMediaBrowserListener.onSearchResultChanged(browser, query, itemCount, params)
        verify(mockMediaBrowserListener, times(1)).onSearchResultChanged(browser, query, itemCount, params)
    }

    @Test
    fun testOnDisconnected() {
        asyncMediaBrowserListener.onDisconnected(mediaController)
        verify(mockMediaBrowserListener, times(1)).onDisconnected(mediaController)
    }

    @Test
    fun testOnSetCustomLayout() {
        val layout = listOf(CommandButton.Builder().build())
        asyncMediaBrowserListener.onSetCustomLayout(mediaController, layout)
        verify(mockMediaBrowserListener, times(1)).onSetCustomLayout(mediaController, layout)
    }

    @Test
    fun testOnAvailableSessionCommandsChanged() {
        val sessionCommands = SessionCommands.Builder().build()
        asyncMediaBrowserListener.onAvailableSessionCommandsChanged(mediaController, sessionCommands)
        verify(mockMediaBrowserListener, times(1)).onAvailableSessionCommandsChanged(mediaController, sessionCommands)
    }

    @Test
    fun testOnCustomCommand() {
        val sessionCommand = SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_SEARCH)
        val args = Bundle()
        asyncMediaBrowserListener.onCustomCommand(mediaController, sessionCommand, args)
        verify(mockMediaBrowserListener, times(1)).onCustomCommand(mediaController, sessionCommand, args)
    }

    @Test
    fun testOnExtrasChanged() {
        val extras = Bundle()
        asyncMediaBrowserListener.onExtrasChanged(mediaController, extras)
        verify(mockMediaBrowserListener, times(1)).onExtrasChanged(mediaController, extras)
    }
}

