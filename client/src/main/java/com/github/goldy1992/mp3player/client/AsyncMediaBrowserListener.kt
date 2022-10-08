package com.github.goldy1992.mp3player.client

import android.os.Bundle
import android.util.Log
import androidx.annotation.IntRange
import androidx.media3.session.*
import androidx.media3.session.MediaLibraryService.LibraryParams
import com.github.goldy1992.mp3player.client.ui.logTag
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class AsyncMediaBrowserListener
    @Inject
    constructor() : MediaBrowser.Listener, LogTagger {

    val listeners : MutableSet<MediaBrowser.Listener> = mutableSetOf()

    override fun onChildrenChanged(
        browser: MediaBrowser,
        parentId: String,
        @IntRange(from = 0.toLong()) itemCount: Int,
        params: LibraryParams?
    ) {
        Log.i(logTag(), "children changed parent: $parentId, itemCount: $itemCount, params $params")
        listeners.forEach { listener -> listener.onChildrenChanged(browser, parentId, itemCount, params) }
    }

    override fun onSearchResultChanged(
        browser: MediaBrowser,
        query: String,
        @IntRange(from = 0.toLong()) itemCount: Int,
        params: LibraryParams?
    ) {
        listeners.forEach { listener -> listener.onSearchResultChanged(browser, query, itemCount, params) }

    }


    override fun onDisconnected(controller: MediaController) {
        listeners.forEach { listener -> listener.onDisconnected(controller) }

    }


    override fun onSetCustomLayout(
        controller: MediaController, layout: List<CommandButton>
    ): ListenableFuture<SessionResult> {
        listeners.forEach { listener -> listener.onSetCustomLayout(controller, layout)
        }
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_ERROR_NOT_SUPPORTED))
    }

    override fun onAvailableSessionCommandsChanged(
        controller: MediaController, commands: SessionCommands
    ) {
        listeners.forEach { listener -> listener.onAvailableSessionCommandsChanged(controller, commands) }
    }

    override fun onCustomCommand(
        controller: MediaController, command: SessionCommand, args: Bundle
    ): ListenableFuture<SessionResult> {
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_ERROR_NOT_SUPPORTED))
    }

    override fun onExtrasChanged(controller: MediaController, extras: Bundle) {
        listeners.forEach { listener -> listener.onExtrasChanged(controller, extras) }

    }

    override fun logTag(): String {
       return "AsyncMediaBrowserListener"
    }

}