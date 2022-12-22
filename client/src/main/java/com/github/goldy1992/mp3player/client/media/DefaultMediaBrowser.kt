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
import com.github.goldy1992.mp3player.client.ui.states.eventholders.*
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils.getAudioSample
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams
import com.github.goldy1992.mp3player.client.utils.QueueUtils.getQueue
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME_KEY
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.TimerUtils
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.apache.commons.lang3.StringUtils.isEmpty
import javax.inject.Inject

/**
 * Default implementation of the [IMediaBrowser].
 */
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

    private val _playerEventsFlow : Flow<PlayerEventHolder> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        val messageListener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                trySend(PlayerEventHolder(player, events))
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
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

    private val _shuffleModeFlow : Flow<Boolean> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        val messageListener = object : Player.Listener {
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                trySend(shuffleModeEnabled)
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }
    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return _shuffleModeFlow
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

    private val _playbackParametersFlow : Flow<PlaybackParameters> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        val messageListener = object : Player.Listener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                trySend(playbackParameters)
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }
    override fun playbackParameters(): Flow<PlaybackParameters> {
        return _playbackParametersFlow
    }

    private val _playbackPositionFlow : Flow<PlaybackPositionEvent> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        val messageListener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.i(logTag(), "onIsPlayingChanged: $isPlaying")
                trySend(PlaybackPositionEvent(isPlaying, controller.currentPosition, TimerUtils.getSystemTime()))
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                val isPlaying = controller.isPlaying
                trySend(PlaybackPositionEvent(isPlaying, controller.currentPosition, TimerUtils.getSystemTime()))
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            controller.removeListener(messageListener)
        }
    }
    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return _playbackPositionFlow
    }

    private val _playbackSpeedFlow : Flow<Float> = _playbackParametersFlow
        .map {
            it.speed
        }
    override fun playbackSpeed(): Flow<Float> {
        return _playbackSpeedFlow
    }

    private val events : @Player.Event IntArray = intArrayOf(
        Player.EVENT_MEDIA_ITEM_TRANSITION,
        Player.EVENT_TIMELINE_CHANGED
    )
    private val _queueFlow : Flow<QueueState> = _playerEventsFlow
        .filter { it.events.containsAny( *events ) }
        .map {
            getQueue(it.player!!)
        }

    override fun queue(): Flow<QueueState> {
        return _queueFlow
    }

    private val _repeatModeFlow : Flow<@Player.RepeatMode Int> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        val messageListener = object : Player.Listener {
            override fun onRepeatModeChanged(@Player.RepeatMode repeatMode: Int) {
                trySend(repeatMode)
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }
    override fun repeatMode(): Flow<Int> {
        return _repeatModeFlow
    }

    override suspend fun changePlaybackSpeed(speed: Float) {
        val extras = Bundle()
        extras.putFloat(CHANGE_PLAYBACK_SPEED, speed)
        val changePlaybackSpeedCommand = SessionCommand(CHANGE_PLAYBACK_SPEED, extras)
        mediaBrowserFuture.await().sendCustomCommand(changePlaybackSpeedCommand, extras).await()

    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        return if (pageSize < 1) {
            emptyList()
        } else {
            val children: LibraryResult<ImmutableList<MediaItem>> =
                mediaBrowserFuture.await().getChildren(parentId, page, pageSize, params).await()
            return children.value?.toList() ?: emptyList()
        }
    }

    override suspend fun getLibraryRoot(): MediaItem {
        val args = Bundle()
        args.putString(PACKAGE_NAME_KEY, PACKAGE_NAME)
        val params = MediaLibraryService.LibraryParams.Builder().setExtras(args).build()
        val result = mediaBrowserFuture.await().getLibraryRoot(params).await()
        return result.value ?: MediaItem.EMPTY
    }

    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {
        if (isEmpty(query)) {
            Log.i(logTag(), "getSearchResults called with query \"\"")
            return ImmutableList.of()
        }
        val result : LibraryResult<ImmutableList<MediaItem>> =
            mediaBrowserFuture.await().getSearchResult(query, page, pageSize, getDefaultLibraryParams()).await()
        return result.value ?: ImmutableList.of()
    }

    override suspend fun pause() {
        mediaBrowserFuture.await().pause()
    }

    override suspend fun play() {
        mediaBrowserFuture.await().play()
    }

    override suspend fun play(mediaItem: MediaItem) {
        val mediaBrowser = mediaBrowserFuture.await()
        mediaBrowser.addMediaItem(mediaItem)
        mediaBrowser.prepare()
        mediaBrowser.play()

    }

    override suspend fun playFromSongList(itemIndex: Int, items: List<MediaItem>) {
        val mediaBrowser = mediaBrowserFuture.await()
        mediaBrowser.clearMediaItems()
        mediaBrowser.addMediaItems(items)
        mediaBrowser.seekTo(itemIndex, 0L)
        mediaBrowser.prepare()
        mediaBrowser.play()
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        val mediaItem = MediaItem.Builder().setUri(uri).build()
        mediaBrowserFuture.await().addMediaItem(mediaItem)
    }

    override suspend fun prepareFromMediaId(mediaItem: MediaItem) {
        // call from application looper
        val mediaController = mediaBrowserFuture.await()
        mediaController.addMediaItem(mediaItem)
        mediaController.prepare()
    }

    override suspend fun search(query: String, extras: Bundle) {
        mediaBrowserFuture.await()
            .search(query, MediaLibraryService
                .LibraryParams
                .Builder()
                .setExtras(extras)
                .build())
    }

    override suspend fun seekTo(position: Long) {
        mediaBrowserFuture.await().seekTo(position)
    }

    override suspend fun setRepeatMode(repeatMode: Int) {
        mediaBrowserFuture.await().repeatMode = repeatMode
    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {
        mediaBrowserFuture.await().shuffleModeEnabled = shuffleModeEnabled
    }

    override suspend fun skipToNext() {
        mediaBrowserFuture.await().seekToNextMediaItem()
    }

    override suspend fun skipToPrevious() {
        mediaBrowserFuture.await().seekToPreviousMediaItem()
    }

    override suspend fun stop() {
        mediaBrowserFuture.await().stop()
    }

    override suspend fun subscribe(id: String) {
        mediaBrowserFuture.await().subscribe(id, MediaLibraryService.LibraryParams.Builder().build())
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