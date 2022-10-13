package com.github.goldy1992.mp3player.service

import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Rating
import androidx.media3.session.*
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession.ConnectionResult
import androidx.media3.session.SessionCommand.COMMAND_CODE_CUSTOM
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MetaDataKeys
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.CustomMediaItemTree
import com.github.goldy1992.mp3player.service.player.ChangeSpeedProvider
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@ServiceScoped
class MediaLibrarySessionCallback

    @Inject
    constructor(private val contentManager: ContentManager,
                private val changeSpeedProvider: ChangeSpeedProvider,
                private val rootAuthenticator: RootAuthenticator,
                private val customMediaItemTree: CustomMediaItemTree,
                private val scope : CoroutineScope) : MediaLibrarySession.Callback, LogTagger {

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): ConnectionResult {
        Log.i(logTag(), "on Connect called")
        val connectionResult = super.onConnect(session, controller)
        // add change playback speed command to list of available commands
        val sessionCommand = SessionCommand(CHANGE_PLAYBACK_SPEED, Bundle())
        val updatedSessionCommands = connectionResult.availableSessionCommands.buildUpon().add(sessionCommand).build()
        return ConnectionResult.accept(updatedSessionCommands,connectionResult.availablePlayerCommands)
    }

    override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
        super.onPostConnect(session, controller)
    }

    override fun onDisconnected(session: MediaSession, controller: MediaSession.ControllerInfo) {
        super.onDisconnected(session, controller)
    }

    override fun onPlayerCommandRequest(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        playerCommand: Int
    ): Int {
        return super.onPlayerCommandRequest(session, controller, playerCommand)
    }

    override fun onSetRating(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        rating: Rating
    ): ListenableFuture<SessionResult> {
        return super.onSetRating(session, controller, rating)
    }

    override fun onSetRating(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaId: String,
        rating: Rating
    ): ListenableFuture<SessionResult> {
        return super.onSetRating(session, controller, mediaId, rating)
    }

    override fun onSubscribe(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        var mediaItems = emptyList<MediaItem>()
        runBlocking {
            launch(Dispatchers.Default) {
                // Assume for example that the music catalog is already loaded/cached.
                mediaItems = customMediaItemTree.getChildren(parentId)
                println("finish coroutine")
            }.join()
            println("finished on load children")
        }
        Log.i(logTag(), "notifying children changed for browser ${browser}")
        session.notifyChildrenChanged(browser, parentId, mediaItems.size, params)
        return Futures.immediateFuture(LibraryResult.ofVoid())
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        Log.i(logTag(), "On Custom Command: ${customCommand}, args: ${args}")
        if (Constants.CHANGE_PLAYBACK_SPEED == customCommand.customAction) {
            changeSpeedProvider.changeSpeed(session.player, args)
        }
        return super.onCustomCommand(session, controller, customCommand, args)
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        return Futures.immediateFuture(
            customMediaItemTree.getMediaItems(mediaItems.map(MediaItem::mediaId)).toMutableList()
        )
    }

    override fun onGetLibraryRoot(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
       return Futures.immediateFuture(rootAuthenticator.authenticate(params ?: MediaLibraryService.LibraryParams.Builder().build()))
    }

    override fun onGetChildren(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        if (rootAuthenticator.rejectRootSubscription(parentId)) {
            return Futures.immediateFuture(LibraryResult.ofItemList(emptyList(), params))
        }
        var mediaItems = emptyList<MediaItem>()
        runBlocking {
            launch(Dispatchers.Default) {
                // Assume for example that the music catalog is already loaded/cached.
                mediaItems = customMediaItemTree.getChildren(parentId)
                println("finish coroutine")
            }.join()
            println("finished on load children")
        }
        Log.i(logTag(), "notifying children changed for browser ${browser}")
        return Futures.immediateFuture(LibraryResult.ofItemList(mediaItems, params))
    }



    override fun onSearch(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        val result = contentManager.search(query)
        session.notifySearchResultChanged(browser,query, result.size, params)
        return Futures.immediateFuture(LibraryResult.ofVoid())

    }

    override fun onGetSearchResult(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        // TODO return search result
        return super.onGetSearchResult(session, browser, query, page, pageSize, params)
    }

    override fun logTag(): String {
        return "MediaLibrarySessionCallback"
    }

}