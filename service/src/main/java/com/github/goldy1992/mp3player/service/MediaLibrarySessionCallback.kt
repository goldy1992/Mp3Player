package com.github.goldy1992.mp3player.service

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Rating
import androidx.media3.session.*
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@ServiceScoped
class MediaLibrarySessionCallback

    @Inject
    constructor(private val contentManager: ContentManager) : MediaLibrarySession.Callback {

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        return super.onConnect(session, controller)
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
        return super.onSubscribe(session, browser, parentId, params)
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        customCommand.
        return super.onCustomCommand(session, controller, customCommand, args)
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        return super.onAddMediaItems(mediaSession, controller, mediaItems)
    }

    override fun onGetLibraryRoot(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        //   return rootAuthenticator!!.authenticate(clientPackageName, clientUid, rootHints)
        return super.onGetLibraryRoot(session, browser, params)

    }

    override fun onGetChildren(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
//        if (rootAuthenticator!!.rejectRootSubscription(parentId)) {
//            result.sendResult(null)
//            return
//        }
        runBlocking {
            launch(Dispatchers.Default) {
                // Assume for example that the music catalog is already loaded/cached.
                val mediaItems = contentManager.getChildren(parentId)
                println("finish coroutine")
            }.join()
            println("finished on load children")
        }
        return super.onGetChildren(session, browser, parentId, page, pageSize, params)
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

}