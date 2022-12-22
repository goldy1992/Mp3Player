package com.github.goldy1992.mp3player.client.media

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.IntRange
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.*
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils.getAudioSample
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultMediaBrowser
    @Inject
    constructor(
        @ApplicationContext context: Context,
        sessionToken: SessionToken) : IMediaBrowser, MediaBrowser.Listener, LogTagger {

    private val mediaBrowserFuture: ListenableFuture<MediaBrowser>

    init {
        mediaBrowserFuture =
            MediaBrowser
                .Builder(context, sessionToken)
                .setListener(this)
                .buildAsync()
    }

    private val _onCustomCommandFlow : Flow<SessionCommandEventHolder> = callbackFlow<SessionCommandEventHolder> {
        val messageListener = object : MediaBrowser.Listener {
            override fun onCustomCommand(
                controller: MediaController,
                command: SessionCommand,
                args: Bundle
            ): ListenableFuture<SessionResult> {
                Log.i(logTag(), "onCustomCommand")
                trySend(SessionCommandEventHolder(controller, command, args))
                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
            }
        }
        listeners.add(messageListener)
        awaitClose {
            listeners.remove(messageListener)
        }
    }

    private val _metadataFlow : Flow<MediaMetadata> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        val messageListener = object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                Log.i(logTag(), "onMediaMetadataChanged: $mediaMetadata")
                trySend(mediaMetadata)
            }
        }
        controller.addListener(messageListener)
        awaitClose {

            controller.removeListener(messageListener)
        }
    }

    private val audioDataFlow: Flow<AudioSample> = _onCustomCommandFlow
        .filter {
            Log.i(logTag(), "audioDataFlow filter")
            Constants.AUDIO_DATA == it.command.customAction
        }.map {
            getAudioSample(it)
        }

    override fun audioData(): Flow<AudioSample> {
        return audioDataFlow
    }

    private val _currentMediaItemFlow : Flow<MediaItem> = _metadataFlow.map {
        val mediaItem : MediaItem? = mediaBrowserFuture.await().currentMediaItem
        if (mediaItem == null) {
            Log.w(logTag(), "Current MediaItem is null")
            MediaItem.EMPTY
        } else {
            mediaItem
        }
    }
    override fun currentMediaItem(): Flow<MediaItem> {
        return _currentMediaItemFlow
    }

    override fun currentSearchQuery(): Flow<String> {
        TODO("Not yet implemented")
    }

    private val _isPlayingFlow : Flow<Boolean> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        val messageListener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.i(logTag(), "onIsPlayingChanged: $isPlaying")
                trySend(isPlaying)
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            controller.removeListener(messageListener)
        }
    }
    override fun isPlaying(): Flow<Boolean> {
        return _isPlayingFlow
    }

    override fun isShuffleModeEnabled(): Flow<Boolean> {
        TODO("Not yet implemented")
    }


    override fun metadata(): Flow<MediaMetadata> {
        return _metadataFlow
    }


    private val _onChildrenChangedFlow : Flow<OnChildrenChangedEventHolder> = callbackFlow<OnChildrenChangedEventHolder> {
        val messageListener = object : MediaBrowser.Listener {
            override fun onChildrenChanged(
                browser: MediaBrowser,
                parentId: String,
                itemCount: Int,
                params: MediaLibraryService.LibraryParams?
            ) {
                val x = OnChildrenChangedEventHolder(browser, parentId, itemCount, params)
                trySend(x)
            }
        }
        listeners.add(messageListener)
        awaitClose {
            listeners.remove(messageListener) }
    }
     override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return _onChildrenChangedFlow
    }



    override fun onCustomCommand(): Flow<SessionCommandEventHolder> {
        return _onCustomCommandFlow
    }

    private val _onSearchResultChangedFlow : Flow<OnSearchResultsChangedEventHolder> = callbackFlow {
        val messageListener = object : MediaBrowser.Listener {
            override fun onSearchResultChanged(
                browser: MediaBrowser,
                query: String,
                itemCount: Int,
                params: MediaLibraryService.LibraryParams?
            ) {
                trySend(OnSearchResultsChangedEventHolder(browser, query, itemCount, params))
            }
        }
        listeners.add(messageListener)
        awaitClose()
    }
    override fun onSearchResultsChanged(): Flow<OnSearchResultsChangedEventHolder> {
        return _onSearchResultChangedFlow
    }

    override fun playbackParameters(): Flow<PlaybackParameters> {
        TODO("Not yet implemented")
    }

    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        TODO("Not yet implemented")
    }

    override fun playbackSpeed(): Flow<Float> {
        TODO("Not yet implemented")
    }

    override fun queue(): Flow<QueueState> {
        TODO("Not yet implemented")
    }

    override fun repeatMode(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun changePlaybackSpeed(speed: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getLibraryRoot(): MediaItem {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {
        TODO("Not yet implemented")
    }

    override suspend fun pause() {
        TODO("Not yet implemented")
    }

    override suspend fun play() {
        TODO("Not yet implemented")
    }

    override suspend fun play(mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override suspend fun playFromSongList(itemIndex: Int, items: List<MediaItem>) {
        TODO("Not yet implemented")
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override suspend fun prepareFromMediaId(mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override suspend fun search(query: String, extras: Bundle) {
        TODO("Not yet implemented")
    }

    override suspend fun seekTo(position: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun setRepeatMode(repeatMode: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun skipToNext() {
        TODO("Not yet implemented")
    }

    override suspend fun skipToPrevious() {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }

    override suspend fun subscribe(id: String) {
        TODO("Not yet implemented")
    }



    // The set of all listeners which are made by the Callback Flows
    private val listeners : MutableSet<MediaBrowser.Listener> = mutableSetOf()

    override fun onChildrenChanged(
        browser: MediaBrowser,
        parentId: String,
        @IntRange(from = 0.toLong()) itemCount: Int,
        params: MediaLibraryService.LibraryParams?
    ) {
        Log.i(logTag(), "children changed parent: $parentId, itemCount: $itemCount, params $params")
        listeners.forEach { listener -> listener.onChildrenChanged(browser, parentId, itemCount, params) }
    }

    override fun onSearchResultChanged(
        browser: MediaBrowser,
        query: String,
        @IntRange(from = 0.toLong()) itemCount: Int,
        params: MediaLibraryService.LibraryParams?
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
        listeners.forEach { listener -> listener.onCustomCommand(controller, command, args) }
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_ERROR_NOT_SUPPORTED))
    }

    override fun onExtrasChanged(controller: MediaController, extras: Bundle) {
        listeners.forEach { listener -> listener.onExtrasChanged(controller, extras) }

    }

    override fun logTag(): String {
        return "DefaultMediaBrowser"
    }

}