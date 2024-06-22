package com.github.goldy1992.mp3player.service

import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.*
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession.ConnectionResult
import com.github.goldy1992.mp3player.commons.Constants.AUDIO_DATA
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.Constants.HAS_PERMISSIONS
import com.github.goldy1992.mp3player.commons.IoDispatcher
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.ServiceCoroutineScope
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.player.ChangeSpeedProvider
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import androidx.annotation.OptIn as AndroidXOptIn

@AndroidXOptIn(UnstableApi::class)
@ServiceScoped
class MediaLibrarySessionCallback

    @Inject
    constructor(private val contentManager: ContentManager,
                private val changeSpeedProvider: ChangeSpeedProvider,
                private val rootAuthenticator: RootAuthenticator,
                @ServiceCoroutineScope private val scope : CoroutineScope,
                @IoDispatcher private val ioDispatcher: CoroutineDispatcher) : MediaLibrarySession.Callback, LogTagger {

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): ConnectionResult {
        Log.v(logTag(), "onConnect() invoked with mediaSession ${session.player} and controller ${controller.uid}")
        val connectionResult = super.onConnect(session, controller)
        // add change playback speed command to list of available commands
        val changePlaybackSpeed = SessionCommand(CHANGE_PLAYBACK_SPEED, Bundle())
        val audioDataCommand = SessionCommand(AUDIO_DATA, Bundle())
        val updatedSessionCommands = connectionResult
            .availableSessionCommands
            .buildUpon()
            .add(changePlaybackSpeed)
            .add(audioDataCommand)
            .build()
        val updatedPlayerCommands = connectionResult
            .availablePlayerCommands
            .buildUpon()
            .add(Player.COMMAND_SET_PLAYLIST_METADATA)
            .build()
        return ConnectionResult.accept(updatedSessionCommands,updatedPlayerCommands)
    }

    override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
        Log.v(logTag(), "onPostConnect() calling Player.prepare() for MediaSession Player: ${session.player}")
    }

    override fun onPlayerCommandRequest(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        playerCommand: Int
    ): Int {
        Log.d(logTag(), "player command requested: $playerCommand")
        /* Bug on some Android devices when notification is removed when playback is paused! This causes
             stop() to be called in some unwanted instances, meaning it should be ensured that
             [Player#prepare()] is called beforehand
         */
        session.player.prepare()
        return super.onPlayerCommandRequest(session, controller, playerCommand)
    }

    override fun onGetItem(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        mediaId: String
    ): ListenableFuture<LibraryResult<MediaItem>> {
        Log.v(logTag(), "onGetItem() invoked with mediaId: $mediaId")
        var mediaItem : MediaItem?
        runBlocking(ioDispatcher) {
            mediaItem = contentManager.getContentById(mediaId)
        }
        if (mediaItem == null) {
            Log.w(logTag(), "onGetItem() No media item found for mediaId: $mediaId, returning empty media item")
            mediaItem = MediaItem.EMPTY
        }
        return Futures.immediateFuture(LibraryResult.ofItem(mediaItem!!, MediaLibraryService.LibraryParams.Builder().build()))
    }

    override fun onSubscribe(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        Log.v(logTag(), "onSubscribe() invoked for parentId: $parentId")
        scope.launch(ioDispatcher) {
            // Assume for example that the music catalog is already loaded/cached.
            val contentManagerResult = contentManager.getChildren(parentId)
            var numberOfResults = contentManagerResult.numberOfResults

            if (numberOfResults <= 0) {
                Log.i(logTag(), "onSubscribe() No results found or parentId $parentId")
                // set the number of results to one to account for the empty media item
                numberOfResults = 1
            }

            val extras = Bundle()
            extras.putAll(params?.extras)
            extras.putBoolean(HAS_PERMISSIONS, contentManagerResult.hasPermissions)
            val returnParams = MediaLibraryService.LibraryParams.Builder()
                .setExtras(extras)
                .build()
            Log.d(logTag(), "onSubscribe() notifying children changed for browser $browser")
            session.notifyChildrenChanged(browser, parentId, numberOfResults, returnParams)
            Log.d(logTag(), "session notified")
        }
        return Futures.immediateFuture(LibraryResult.ofVoid())
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
       // Log.v(logTag(), "onCustomCommand() invoked with command: ${customCommand}, args: $args")
        if (CHANGE_PLAYBACK_SPEED == customCommand.customAction) {
            Log.d(logTag(), "onCustomCommand() identified as CHANGE_PLAYBACK_SPEED")
            changeSpeedProvider.changeSpeed(session.player, args)
            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        }
        return super.onCustomCommand(session, controller, customCommand, args)
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        Log.v(logTag(), "onAddMediaItems() invoked with the mediaItems: ${mediaItems.map(MediaItem::mediaId).joinToString(",")}}")
        super.onAddMediaItems(mediaSession, controller, mediaItems)
        val toReturn = mutableListOf<MediaItem>()
        runBlocking {
            for (item in mediaItems) {
                toReturn.add(contentManager.getContentById(item.mediaId))
            }
        }
        Log.v(logTag(), "onAddMediaItems() returning with the mediaItems: ${toReturn.map(MediaItem::mediaId).joinToString(",")}}")
        return Futures.immediateFuture(toReturn)
    }

    override fun onGetLibraryRoot(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        Log.v(logTag(), "onGetLibraryRoot() invoked.")
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
        Log.v(logTag(), "onGetChildren() invoked for parentId: $parentId, page: $page, $pageSize pageSize")
        if (rootAuthenticator.rejectRootSubscription(parentId)) {
            Log.w(logTag(), "onGetChildren() rejected for parentId: $parentId")
            return Futures.immediateFuture(LibraryResult.ofItemList(emptyList(), params))
        }
        var mediaItems: List<MediaItem>
        runBlocking(ioDispatcher) {
                // Assume for example that the music catalog is already loaded/cached.
                mediaItems = contentManager.getChildren(parentId).children
                if (mediaItems.isEmpty()) {
                    Log.w(logTag(), "onGetChildren() No results found for parentId $parentId")
                    // set the number of results to one to account for the empty media item
                }
        }
        Log.v(logTag(), "onGetChildren() notifying children changed for browser $browser")
        return Futures.immediateFuture(LibraryResult.ofItemList(mediaItems, params))
    }

    override fun onSearch(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        Log.v(logTag(), "onSearch() invoked with query: $query")
        scope.launch(ioDispatcher) {
           val result = contentManager.search(query)
            Log.d(logTag(), "onSearch() ${result.size} results found query: $query")
            session.notifySearchResultChanged(browser, query, result.size, params)
        }
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
        Log.v(logTag(), "onGetSearchResult() invoked for query: $query, page: $page, pageSize: $pageSize")
        var searchResults : List<MediaItem> = ArrayList()
        runBlocking {
            val searchJob = scope.launch(ioDispatcher) {
                searchResults = contentManager.search(query)
            }
            searchJob.join()
        }
        return Futures.immediateFuture(LibraryResult.ofItemList(searchResults, params))
    }

    override fun onDisconnected(session: MediaSession, controller: MediaSession.ControllerInfo) {
        Log.v(logTag(), "onDisconnected invoked with session: $session")
        super.onDisconnected(session, controller)
    }

    override fun logTag(): String {
        return "MediaLibrarySessionCallback"
    }

}