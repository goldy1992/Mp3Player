package com.github.goldy1992.mp3player.client.media

import android.annotation.SuppressLint
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.LibraryParams
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionCommands
import androidx.media3.session.SessionResult
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnPlaybackPositionChangedEvent
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnQueueChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.media.flows.AudioDataFlow
import com.github.goldy1992.mp3player.client.media.flows.CurrentMediaItemFlow
import com.github.goldy1992.mp3player.client.media.flows.CurrentPlaylistMetadataFlow
import com.github.goldy1992.mp3player.client.media.flows.IsPlayingFlow
import com.github.goldy1992.mp3player.client.media.flows.MetadataFlow
import com.github.goldy1992.mp3player.client.media.flows.OnChildrenChangedFlow
import com.github.goldy1992.mp3player.client.media.flows.OnCustomCommandFlow
import com.github.goldy1992.mp3player.client.media.flows.OnSearchResultsChangedFlow
import com.github.goldy1992.mp3player.client.media.flows.PlaybackParametersFlow
import com.github.goldy1992.mp3player.client.media.flows.PlaybackPositionFlow
import com.github.goldy1992.mp3player.client.media.flows.PlayerEventsFlow
import com.github.goldy1992.mp3player.client.media.flows.QueueFlow
import com.github.goldy1992.mp3player.client.media.flows.RepeatModeFlow
import com.github.goldy1992.mp3player.client.media.flows.ShuffleModeFlow
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants.CHANGE_PLAYBACK_SPEED
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME
import com.github.goldy1992.mp3player.commons.Constants.PACKAGE_NAME_KEY
import com.github.goldy1992.mp3player.commons.Constants.PLAYLIST_ID
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.LoggingUtils
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.commons.lang3.StringUtils.isEmpty
import androidx.annotation.OptIn as AndroidXOptIn

/**
 * Default implementation of the [IMediaBrowser].
 */
class DefaultMediaBrowser

    constructor(
        @ApplicationContext private val context: Context,
        @MainDispatcher private val mainDispatcher : CoroutineDispatcher
    ) : IMediaBrowser, MediaBrowser.Listener, LogTagger {

    private lateinit var scope : CoroutineScope
    private val _mediaBrowserLFMutableStateFlow: MutableStateFlow<ListenableFuture<MediaBrowser>?> =
        MutableStateFlow(null)

    @Suppress("UNCHECKED_CAST")
    override fun init(sessionToken: SessionToken, scope : CoroutineScope) {
        Log.v(logTag(), "init() invoked")
        this.scope = scope
        _mediaBrowserLFMutableStateFlow.value = MediaBrowser
            .Builder(context, sessionToken)
            .setListener(this)
            .buildAsync()

        scope.launch {
            Log.d(logTag(), "scope.launch")
            _mediaBrowserLFMutableStateFlow.filterNotNull().collect {
                val playerf = it as ListenableFuture<Player>
                Log.d(logTag(), "collecting from mediaBrowserLFSF")
                AudioDataFlow.create(scope, _customCommandMutableStateFlow) { a : AudioSample ->_audioDataMutableStateFlow.value = a }
                PlayerEventsFlow.create(scope, it, mainDispatcher) { v -> _playerEventMSF.emit(v) }
                CurrentMediaItemFlow.create(scope, _metadataMutableStateFlow, playerf, mainDispatcher) {v -> _currentMediaItemFlowMutableStateFlow.value = v}
                CurrentPlaylistMetadataFlow.create(scope, it, mainDispatcher) { v -> _currentPlaylistMetadataMutableStateFlow.value = v }
                IsPlayingFlow.create(scope, it, mainDispatcher) { v -> _isPlayingMutableStateFlow.value = v }
                MetadataFlow.create(scope, it, mainDispatcher) { m -> _metadataMutableStateFlow.value = m }
                OnChildrenChangedFlow.create(scope, addListener, removeListener ) { v -> _onChildrenChangedMutableSharedFlow.emit(v) }
                OnCustomCommandFlow.create(scope, addListener, removeListener) { c -> _customCommandMutableStateFlow.emit(c) }
                OnSearchResultsChangedFlow.create(scope, addListener, removeListener) { v -> _onSearchResultsChangedMutableStateFlow.value = v }
                PlaybackParametersFlow.create(scope, it, mainDispatcher){v-> _playbackParametersMutableStateFlow.value = v}
                PlaybackPositionFlow.create(scope, it, mainDispatcher) { v-> _playbackPositionMutableStateFlow.value = v }
                QueueFlow.create(it, mainDispatcher, scope) { v -> _queueMutableStateFlow.value = v}
                RepeatModeFlow.create(scope, it, mainDispatcher) { v -> _repeatModeMutableStateFlow.value = v}
                ShuffleModeFlow.create(scope, it, mainDispatcher) { v -> _shuffleModeMutableStateFlow.value = v}
                Log.d(logTag(), "finished mediaBrowserLFSF.collect")
            }
        }
    }

    private val _audioDataMutableStateFlow = MutableStateFlow(AudioSample.NONE)
    override fun audioData(): Flow<AudioSample> {
        return _audioDataMutableStateFlow.asStateFlow()
    }

    private val _currentMediaItemFlowMutableStateFlow = MutableStateFlow(MediaItem.EMPTY)
    override fun currentMediaItem(): Flow<MediaItem> {
        return _currentMediaItemFlowMutableStateFlow.asStateFlow()
    }

    private val _currentPlaylistMetadataMutableStateFlow = MutableStateFlow(MediaMetadata.EMPTY)
    override fun currentPlaylistMetadata(): Flow<MediaMetadata> {
        return _currentPlaylistMetadataMutableStateFlow.asStateFlow()
    }

    private val _isPlayingMutableStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override fun isPlaying(): Flow<Boolean> {
        return _isPlayingMutableStateFlow.asStateFlow()
    }

    private val _shuffleModeMutableStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return _shuffleModeMutableStateFlow.asStateFlow()
    }

    private val _metadataMutableStateFlow = MutableStateFlow(MediaMetadata.EMPTY)
    override fun metadata(): Flow<MediaMetadata> {
        return _metadataMutableStateFlow.asStateFlow()
    }

    // use a SharedFlow for onChildrenChanged
    private val _onChildrenChangedMutableSharedFlow = MutableSharedFlow<OnChildrenChangedEventHolder>(replay = 1)
    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return _onChildrenChangedMutableSharedFlow.asSharedFlow()
    }

    private val _customCommandMutableStateFlow = MutableSharedFlow<SessionCommandEventHolder>()
    override fun onCustomCommand(): Flow<SessionCommandEventHolder> {
        return _customCommandMutableStateFlow.asSharedFlow()
    }


    private val _playerEventMSF = MutableSharedFlow<Player.Events>()
    private val _onSearchResultsChangedMutableStateFlow = MutableStateFlow(
        OnSearchResultsChangedEventHolder.DEFAULT)

    override fun onSearchResultsChanged(): Flow<OnSearchResultsChangedEventHolder> {
        return _onSearchResultsChangedMutableStateFlow.asStateFlow()
    }

    private val _playbackParametersMutableStateFlow = MutableStateFlow(PlaybackParameters.DEFAULT)
    override fun playbackParameters(): Flow<PlaybackParameters> {
        return _playbackParametersMutableStateFlow.asStateFlow()
    }

    private val _playbackPositionMutableStateFlow = MutableStateFlow(OnPlaybackPositionChangedEvent.DEFAULT)
    override fun playbackPosition(): Flow<OnPlaybackPositionChangedEvent> {
        return _playbackPositionMutableStateFlow.asStateFlow()
    }

    private val _playbackSpeedFlow : Flow<Float> = _playbackParametersMutableStateFlow
        .map {
            it.speed
        }

    override fun playbackSpeed(): Flow<Float> {
        return _playbackSpeedFlow
    }

    private val _queueMutableStateFlow = MutableStateFlow(OnQueueChangedEventHolder.EMPTY)
    override fun queue(): Flow<OnQueueChangedEventHolder> {
        return _queueMutableStateFlow.asStateFlow()
    }

    private val _repeatModeMutableStateFlow: MutableStateFlow<@Player.RepeatMode Int> = MutableStateFlow(Player.REPEAT_MODE_OFF)
    override fun repeatMode(): Flow<@Player.RepeatMode Int> {
        return _repeatModeMutableStateFlow.asStateFlow()
    }

    override suspend fun changePlaybackSpeed(speed: Float) {
        val extras = Bundle()
        extras.putFloat(CHANGE_PLAYBACK_SPEED, speed)
        val changePlaybackSpeedCommand = SessionCommand(CHANGE_PLAYBACK_SPEED, extras)
        _mediaBrowserLFMutableStateFlow.value?.await()?.sendCustomCommand(changePlaybackSpeedCommand, extras)?.await()

    }

    override suspend fun getCurrentPlaybackPosition(): Long {
        var toReturn : Long
        val mediaBrowser = _mediaBrowserLFMutableStateFlow.value?.await()
        runBlocking(mainDispatcher) {
            toReturn = mediaBrowser?.currentPosition ?: 0L
        }
        return toReturn
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: LibraryParams
    ): List<MediaItem> {
        return if (pageSize < 1) {
            emptyList()
        } else {
            val children: LibraryResult<ImmutableList<MediaItem>> =
                _mediaBrowserLFMutableStateFlow.value?.await()?.getChildren(parentId, page, pageSize, params)?.await()?: LibraryResult.ofItemList(
                    listOf(), LibraryParams.Builder().build()
                )
            return children.value?.toList() ?: emptyList()
        }
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    override suspend fun getLibraryRoot(): MediaItem {
        val args = Bundle()
        args.putString(PACKAGE_NAME_KEY, PACKAGE_NAME)
        val params = LibraryParams.Builder()
            .setExtras(args).build()
        val result = _mediaBrowserLFMutableStateFlow.value?.await()?.getLibraryRoot(params)?.await()
        return result?.value ?: MediaItem.EMPTY
    }

    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {
        Log.v(logTag(), "getSearchResults() invoked with query: $query, page: $page, pageSize: $pageSize")
        if (isEmpty(query)) {
            Log.w(logTag(), "getSearchResults() called with empty query")
            return listOf()
        }
        val result : LibraryResult<ImmutableList<MediaItem>> =
            _mediaBrowserLFMutableStateFlow.value?.await()?.getSearchResult(query, page, pageSize, getDefaultLibraryParams())?.await()?: LibraryResult.ofItemList(
                listOf(), LibraryParams.Builder().build()
            )
        return result.value ?: listOf()
    }

    override suspend fun pause() {
        _mediaBrowserLFMutableStateFlow.value?.await()?.pause()
    }

    override suspend fun play() {
        Log.v(logTag(), "play() invoked, awaiting mediaBrowser")
        val mediaBrowser = _mediaBrowserLFMutableStateFlow.value?.await()
        Log.d(logTag(), "play() mediaBrowser retrieved, isConnected: ${mediaBrowser?.isConnected}, playbackState: ${LoggingUtils.logPlaybackState(mediaBrowser?.playbackState ?: 0, logTag())}")
        mediaBrowser?.play()
        Log.v(logTag(), "play() invocation complete")
    }

    override suspend fun play(mediaItem: MediaItem) {
        val mediaBrowser = _mediaBrowserLFMutableStateFlow.value?.await()
        mediaBrowser?.addMediaItem(mediaItem)
        mediaBrowser?.prepare()
        mediaBrowser?.play()

    }

    override suspend fun playFromPlaylist(items: List<MediaItem>, itemIndex: Int, playlistId: String) {
        Log.v(logTag(), "playFromPlaylist() invoked with MediaMetadata: $playlistId")
        val mediaBrowser = _mediaBrowserLFMutableStateFlow.value?.await()
        mediaBrowser?.setMediaItems(items, itemIndex, 0L)

        Log.d(logTag(), "playFromPlaylist() setting playlist metadata to ${playlistId}")
        mediaBrowser?.play()
        val extras = Bundle()
        extras.putString(PLAYLIST_ID, playlistId)
        mediaBrowser?.playlistMetadata = MediaMetadata.Builder().setExtras(extras).build()

        Log.v(logTag(), "playFromPlaylist() invocation complete")
    }

    override suspend fun playFromPlaylist(playlistId: String, itemIndex: Int) {
        Log.v(logTag(), "playFromPlaylist() invoked with id $playlistId")
        val mediaBrowser = _mediaBrowserLFMutableStateFlow.value?.await()
       mediaBrowser?.getItem(playlistId)
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        val mediaItem = MediaItem.Builder().setUri(uri).build()
        _mediaBrowserLFMutableStateFlow.value?.await()?.addMediaItem(mediaItem)
    }


    override suspend fun prepareFromMediaId(mediaId: String) {
        // call from application looper
        val mediaController = _mediaBrowserLFMutableStateFlow.value?.await()
        val mediaItem = mediaController?.getItem(mediaId)?.await()?.value ?: MediaItem.EMPTY
        mediaController?.addMediaItem(mediaItem)
        mediaController?.prepare()
    }

    @AndroidXOptIn(UnstableApi::class)
    override suspend fun search(query: String, extras: Bundle) {
        _mediaBrowserLFMutableStateFlow.value?.await()
            ?.search(query, MediaLibraryService
                .LibraryParams
                .Builder()
                .setExtras(extras)
                .build())
    }

    override suspend fun seekTo(position: Long) {
        _mediaBrowserLFMutableStateFlow.value?.await()?.seekTo(position)
    }

    override suspend fun setRepeatMode(repeatMode: Int) {
        _mediaBrowserLFMutableStateFlow.value?.await()?.repeatMode = repeatMode
    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {
        Log.v(logTag(), "setShuffleMode() invoked with value: $shuffleModeEnabled")
        _mediaBrowserLFMutableStateFlow.value?.await()?.shuffleModeEnabled = shuffleModeEnabled
    }

    override suspend fun skipToNext() {
        _mediaBrowserLFMutableStateFlow.value?.await()?.seekToNextMediaItem()
    }

    override suspend fun skipToPrevious() {
        _mediaBrowserLFMutableStateFlow.value?.await()?.seekToPreviousMediaItem()
    }

    override suspend fun stop() {
        Log.d(logTag(), "stop inoked")
        _mediaBrowserLFMutableStateFlow.value?.await()?.stop()
    }

    override suspend fun subscribe(id: String) {
        Log.v(logTag(), "subscribe() invoked with id: $id")
        _mediaBrowserLFMutableStateFlow.value?.await()?.subscribe(id, LibraryParams.Builder().build())
    }

    override fun release() {
        Log.v(logTag(), "release() invoked, releasing MediaBrowser future")
        if (_mediaBrowserLFMutableStateFlow.value != null)
            MediaBrowser.releaseFuture(_mediaBrowserLFMutableStateFlow.value!!)
        Log.v(logTag(), "release() finished releasing MediaBrowser future")
    }

    // The set of all listeners which are made by the Callback Flows
    private val listeners : MutableSet<MediaBrowser.Listener> = mutableSetOf()
    private val addListener : (MediaBrowser.Listener) -> Boolean = { listeners.add(it) }
    private val removeListener : (MediaBrowser.Listener) -> Boolean = { listeners.add(it) }

    @SuppressLint("Range")
    override fun onChildrenChanged(
        browser: MediaBrowser,
        parentId: String,
        @IntRange(from = 0.toLong()) itemCount: Int,
        params: LibraryParams?
    ) {
        Log.i(logTag(), "onChildrenChanged() invoked with parent: $parentId, itemCount: $itemCount, params $params")
        listeners.forEach { listener -> listener.onChildrenChanged(browser, parentId, itemCount, params) }
    }

    @SuppressLint("Range")
    override fun onSearchResultChanged(
        browser: MediaBrowser,
        query: String,
        @IntRange(from = 0.toLong()) itemCount: Int,
        params: LibraryParams?
    ) {
        listeners.forEach { listener -> listener.onSearchResultChanged(browser, query, itemCount, params) }
    }

    override fun onDisconnected(controller: MediaController) {
        Log.d(logTag(), "OnDisconnected invoked")
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
        return "DefaultMediaBrowser2"
    }

}