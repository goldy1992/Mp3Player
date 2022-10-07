package com.github.goldy1992.mp3player.client

import androidx.concurrent.futures.await
import androidx.media3.common.FlagSet
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.*
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.eventholders.PlayerEventHolder

import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class AsyncPlayerListener

    @Inject
    constructor(private val mediaControllerFuture: ListenableFuture<MediaController>,
                scope : CoroutineScope) {

    private var mediaController : MediaController? = null

    init {
        scope.launch {
            mediaController = mediaControllerFuture.await()
        }
        scope.launch {
            playbackStateCallbackFlow.collect {
                playbackStateMutableState.value = it
            }
        }
        scope.launch {
            mediaMetadataCallbackFlow.collect {
                mediaMetadataMutableState.value = it
            }
        }
        scope.launch {
            repeatModeCallbackFlow.collect {
                repeatModeMutableState.value = it
            }
        }
        scope.launch {
            shuffleModeCallbackFlow.collect {
                shuffleModeMutableState.value = it
            }
        }
        scope.launch {
            queueFlow.collect {
                queueMutableState.value = it
            }
        }
        scope.launch {
            isPlayingFlow.collect {
                isPlayingMutableState.value = it
            }
        }
    }

    private val playbackStateCallbackFlow : Flow<Int> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                trySend(playbackState)
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    private val playbackStateMutableState = MutableStateFlow(Player.STATE_IDLE)
    val playbackStateFlow : StateFlow<Int> = playbackStateMutableState


    private val mediaMetadataCallbackFlow : Flow<MediaMetadata> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                trySend(mediaMetadata)
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    private val mediaMetadataMutableState = MutableStateFlow(MediaMetadata.EMPTY)
    val mediaMetadataState : StateFlow<MediaMetadata> = mediaMetadataMutableState




    private val repeatModeCallbackFlow : Flow<Int> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onRepeatModeChanged(repeatMode: Int) {
                trySend(repeatMode)
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    private val repeatModeMutableState = MutableStateFlow(Player.REPEAT_MODE_ALL)
    val repeatModeState : StateFlow<Int> = repeatModeMutableState




    private val shuffleModeCallbackFlow : Flow<Boolean> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                trySend(shuffleModeEnabled)
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    private val shuffleModeMutableState = MutableStateFlow(false)
    val shuffleModeState : StateFlow<Boolean> = shuffleModeMutableState


    private val eventsFlow : Flow<PlayerEventHolder> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                trySend(PlayerEventHolder(player, events))
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )


    private val eventsMutableState = MutableStateFlow(PlayerEventHolder(null, Events(FlagSet.Builder().build())))
    val eventsState : StateFlow<PlayerEventHolder> = eventsMutableState


    private val queueFlow : Flow<List<MediaItem>> = eventsState
        .filter { it.events.contains(EVENT_TIMELINE_CHANGED) }
        .map {
            val playlist = mutableListOf<MediaItem>()
            val count : Int = it.player?.mediaItemCount ?: 0
            for (i in 0 until count) {
                playlist.add(i, it.player!!.getMediaItemAt(i))
            }
            playlist.toList()
        }.shareIn(
            scope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    private val queueMutableState = MutableStateFlow(emptyList<MediaItem>())
    val queueState : StateFlow<List<MediaItem>> = queueMutableState


    private val playbackParamsFlow : Flow<PlaybackParameters> = eventsState
        .filter { it.events.contains(EVENT_PLAYBACK_PARAMETERS_CHANGED) }
        .map {
            it.player?.playbackParameters ?: PlaybackParameters.DEFAULT
        }.shareIn(
            scope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    private val playbackParametersMutableState = MutableStateFlow(PlaybackParameters.DEFAULT)
    val playbackParametersState : StateFlow<PlaybackParameters> = playbackParametersMutableState




    private val isPlayingFlow : Flow<Boolean> = playbackStateFlow
        .map {
            if (it == STATE_READY) {
                mediaControllerFuture.await().isPlaying
            } else
                false
        }.shareIn(
            scope,
            replay = 1,
            started = SharingStarted.WhileSubscribed()
        )

    private val isPlayingMutableState = MutableStateFlow(false)
    val isPlayingState : StateFlow<Boolean> = isPlayingMutableState


    open fun isPlaying() : Boolean {
        return mediaController?.isPlaying ?: false
    }
//    open fun getActiveQueueItemId(): Long? {
//        return playbackState.value?.activeQueueItemId
//    }
//
//    open fun calculateCurrentQueuePosition(): Int {
//        val currentQueue = queue.value
//        val activeQueueItemId = getActiveQueueItemId()
//        if (currentQueue != null) {
//            for (i in currentQueue.indices) {
//                val queueItem = currentQueue[i]
//                if (queueItem.queueId == activeQueueItemId) {
//                    return i
//                }
//            }
//        }
//        return -1
//    }



//    open val playbackSpeed : MutableLiveData<Float> = MutableLiveData(1f)


//    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
//        this.playbackState.postValue(state)
//        val playing = state.state == PlaybackStateCompat.STATE_PLAYING
//        this.isPlaying.postValue(playing)
//        if (playing) {
//            this.playbackSpeed.postValue(state.playbackSpeed)
//        }
//        Log.i(logTag(), "IS PLAYING: $playing")
//    }


//    open fun getCurrentSongAlbumArtUri() : Uri? {
//        val albumArtUriPath = metadata.value!!.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)
//
//        return try {
//            Uri.parse(albumArtUriPath)
//        } catch (ex: NullPointerException) {
//            Log.e(logTag(), "$albumArtUriPath: is an invalid Uri")
//            return null
//        }
//    }

}