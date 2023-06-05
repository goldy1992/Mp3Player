package com.github.goldy1992.mp3player.client.media

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.IntRange
import androidx.annotation.OptIn as AndroidXOptIn
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.*
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.*
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils.getAudioSample
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams
import com.github.goldy1992.mp3player.client.utils.QueueUtils.getQueue
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME_KEY
import com.github.goldy1992.mp3player.commons.Constants.PLAYLIST_ID
import com.github.goldy1992.mp3player.commons.LoggingUtils.getPlayerEventsLogMessage
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.apache.commons.lang3.StringUtils.isEmpty

/**
 * Default implementation of the [IMediaBrowser].
 */
class DefaultMediaBrowser

    constructor(
        @ApplicationContext private val context: Context,
        sessionToken: SessionToken,
        private val scope : CoroutineScope,
        @MainDispatcher private val mainDispatcher : CoroutineDispatcher) : IMediaBrowser, MediaBrowser.Listener, LogTagger {

    private val mediaBrowserFuture: ListenableFuture<MediaBrowser> = MediaBrowser
        .Builder(context, sessionToken)
        .setListener(this)
        .buildAsync()

    private val _onCustomCommandFlow : Flow<SessionCommandEventHolder> = callbackFlow<SessionCommandEventHolder> {
        val messageListener = object : MediaBrowser.Listener {
            override fun onCustomCommand(
                controller: MediaController,
                command: SessionCommand,
                args: Bundle
            ): ListenableFuture<SessionResult> {
                Log.i(logTag(), "onCustomCommand")
                trySend(SessionCommandEventHolder(command, args))
                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
            }
        }
        listeners.add(messageListener)
        awaitClose {
            listeners.remove(messageListener)
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )

    private val _metadataFlow : Flow<MediaMetadata> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        Log.i(logTag(), "event isPlaying mediabrowser awaited")
        var currentMediaMetadata : MediaMetadata
        withContext(mainDispatcher) {
            currentMediaMetadata = controller.mediaMetadata
        }
        trySend(currentMediaMetadata)
        val messageListener = object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                Log.i(logTag(), "onMediaMetadataChanged: $mediaMetadata")
                trySend(mediaMetadata)
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )

    private val audioDataFlow: Flow<AudioSample> = _onCustomCommandFlow
        .filter {
            Log.i(logTag(), "audioDataFlow filter")
            Constants.AUDIO_DATA == it.command.customAction
        }.map {
            getAudioSample(it)
        }.shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1
        )

    override fun audioData(): Flow<AudioSample> {
        return audioDataFlow
    }

    private val _currentMediaItemFlow : Flow<MediaItem> = _metadataFlow.map {
        val mediaBrowser : MediaBrowser = mediaBrowserFuture.await()
        var mediaItem: MediaItem?

        runBlocking(mainDispatcher) {
            mediaItem = mediaBrowser.currentMediaItem
        }
        if (mediaItem == null) {
            Log.w(logTag(), "Current MediaItem is null")
        }
        mediaItem
    }
    .filterNotNull()
    .shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
    override fun currentMediaItem(): Flow<MediaItem> {
        return _currentMediaItemFlow
    }

    private val _currentPlaylistMetadataFlow : Flow<MediaMetadata> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        runBlocking(mainDispatcher) {
            trySend(controller.playlistMetadata)
        }
        val messageListener = object  : Player.Listener {
            override fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {
                Log.i(logTag(), "PlaylistMeta data: ${mediaMetadata.extras?.getString(PLAYLIST_ID) ?: Constants.UNKNOWN}")
                trySend(mediaMetadata)
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }
    .shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
    override fun currentPlaylistMetadata(): Flow<MediaMetadata> {
        return _currentPlaylistMetadataFlow
    }

    private val _isPlayingFlow : Flow<Boolean> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        Log.i(logTag(), "event isPlaying mediabrowser awaited")
        var isPlaying : Boolean
        withContext(mainDispatcher) {
            isPlaying = controller.isPlaying
        }
        trySend(isPlaying)
        val messageListener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.i(logTag(), "onIsPlayingChanged: $isPlaying")
                trySend(isPlaying)
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }.shareIn(
        scope= scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )

    override fun isPlaying(): Flow<Boolean> {
        return _isPlayingFlow
    }

    private val _shuffleModeFlow : Flow<Boolean> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        val messageListener = object : Player.Listener {
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                Log.i(logTag(), "onShuffleModeEnabledChanged invoked with value: $shuffleModeEnabled")
                trySend(shuffleModeEnabled)
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
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
                Log.i(logTag(), "onChildrenChangedFlow invoked - parent: $parentId, itemCount: $itemCount, params $params")
                val x = OnChildrenChangedEventHolder(parentId, itemCount, params)
                trySend(x)
            }
        }
        listeners.add(messageListener)
        awaitClose {
            listeners.remove(messageListener) }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
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
                trySend(OnSearchResultsChangedEventHolder(query, itemCount, params))
            }
        }
        listeners.add(messageListener)
        awaitClose {
            listeners.remove(messageListener)
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
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
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
    override fun playbackParameters(): Flow<PlaybackParameters> {
        return _playbackParametersFlow
    }

    val playbackPositionEvents : @Player.Event IntArray = intArrayOf(
        Player.EVENT_POSITION_DISCONTINUITY,
        Player.EVENT_IS_PLAYING_CHANGED,
        Player.EVENT_PLAYBACK_PARAMETERS_CHANGED
    )

    private val _playbackPositionFlow : Flow<PlaybackPositionEvent> = callbackFlow {
        val controller = mediaBrowserFuture.await()
        withContext(mainDispatcher) {
            trySend(
                PlaybackPositionEvent(
                    controller.isPlaying,
                    controller.currentPosition,
                    TimerUtils.getSystemTime()
                )
            )
        }
        val messageListener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                if (events.containsAny( *playbackPositionEvents )) {
                    val currentPosition = player.currentPosition
                    val isPlaying = player.isPlaying
                    Log.i(logTag(), "playbackPosition changed due to ${getPlayerEventsLogMessage(events)} with position $currentPosition, isPlaying: $isPlaying")
                    trySend(PlaybackPositionEvent(isPlaying, currentPosition, TimerUtils.getSystemTime()))
                }
            }
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                val isPlaying = controller.isPlaying
                val currentPosition = controller.currentPosition


                trySend(PlaybackPositionEvent(isPlaying, currentPosition, TimerUtils.getSystemTime()))
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return _playbackPositionFlow
    }

    private val _playbackSpeedFlow : Flow<Float> = _playbackParametersFlow
        .map {
            it.speed
        }.shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1
        )
    override fun playbackSpeed(): Flow<Float> {
        return _playbackSpeedFlow
    }

    private val events : @Player.Event IntArray = intArrayOf(
        Player.EVENT_MEDIA_ITEM_TRANSITION,
        Player.EVENT_TIMELINE_CHANGED,
        Player.EVENT_TRACKS_CHANGED,
        Player.EVENT_MEDIA_METADATA_CHANGED
    )
    private val _queueFlow : Flow<QueueState> = callbackFlow {

        val controller = mediaBrowserFuture.await()
        var queue: QueueState
        withContext(mainDispatcher) {
            queue = getQueue( controller)
        }

        trySend(queue)

        Log.i(logTag(), "player event controller awaiter")
        val messageListener = object : Player.Listener {
            override fun onEvents(player: Player, event: Player.Events) {
                val e = getPlayerEventsLogMessage(event)
                Log.i(logTag(), "queue event logged ${e}")
                if (event.containsAny( *events )) {

                    trySend(getQueue(player))
                }
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }.shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1
        )

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
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )
    override fun repeatMode(): Flow<Int> {
        return _repeatModeFlow
    }

    override suspend fun changePlaybackSpeed(speed: Float) {
        val extras = Bundle()
        extras.putFloat(CHANGE_PLAYBACK_SPEED, speed)
        val changePlaybackSpeedCommand = SessionCommand(CHANGE_PLAYBACK_SPEED, extras)
        mediaBrowserFuture.await().sendCustomCommand(changePlaybackSpeedCommand, extras).await()

    }

    override suspend fun getCurrentPlaybackPosition(): Long {
        var toReturn : Long
        val mediaBrowser = mediaBrowserFuture.await()
        runBlocking(mainDispatcher) {
            toReturn = mediaBrowser.currentPosition
        }
        return toReturn
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

    @androidx.annotation.OptIn(UnstableApi::class)
    override suspend fun getLibraryRoot(): MediaItem {
        val args = Bundle()
        args.putString(PACKAGE_NAME_KEY, PACKAGE_NAME)
        val params = MediaLibraryService.LibraryParams.Builder()
            .setExtras(args).build()
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
        Log.i(logTag(), "play clicked waiting for mediaBrowser")
        val mediaBrowser = mediaBrowserFuture.await()
        Log.i(logTag(), "mediaBrowser retrieved, calling play")
        LoggingUtils.logPlaybackState(mediaBrowser.playbackState, logTag())
        mediaBrowser.play()
        Log.i(logTag(), "play was called")
    }

    override suspend fun play(mediaItem: MediaItem) {
        val mediaBrowser = mediaBrowserFuture.await()
        mediaBrowser.addMediaItem(mediaItem)
        mediaBrowser.prepare()
        mediaBrowser.play()

    }

    override suspend fun playFromPlaylist(items: List<MediaItem>, itemIndex: Int, playlistMetadata: MediaMetadata) {
        Log.i(logTag(), "Hit playSongFromList with metadata: $playlistMetadata")
        val mediaBrowser = mediaBrowserFuture.await()
        mediaBrowser.setMediaItems(items, itemIndex, 0L)

        Log.i(logTag(), "Set playlist metadata to ${playlistMetadata.extras?.getString(PLAYLIST_ID)}")
        mediaBrowser.play()
        mediaBrowser.playlistMetadata = playlistMetadata

        Log.i(logTag(), "Completed playSongFromList")
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

    @AndroidXOptIn(UnstableApi::class)
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
        Log.i(logTag(), "Setting shuffle mode enabled with value: $shuffleModeEnabled")
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
        Log.d(logTag(), "subscribing to id: $id")
        mediaBrowserFuture.await().subscribe(id, MediaLibraryService.LibraryParams.Builder().build())
    }

    override fun release() {
        Log.i(logTag(), "releasing MediaBrowser")
        MediaBrowser.releaseFuture(mediaBrowserFuture)
        Log.i(logTag(), "MediaBrowser released")
    }

    // The set of all listeners which are made by the Callback Flows
    private val listeners : MutableSet<MediaBrowser.Listener> = mutableSetOf()

    @SuppressLint("Range")
    override fun onChildrenChanged(
        browser: MediaBrowser,
        parentId: String,
        @IntRange(from = 0.toLong()) itemCount: Int,
        params: MediaLibraryService.LibraryParams?
    ) {
        Log.i(logTag(), "children changed callback method parent: $parentId, itemCount: $itemCount, params $params")
        listeners.forEach { listener -> listener.onChildrenChanged(browser, parentId, itemCount, params) }
    }

    @SuppressLint("Range")
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