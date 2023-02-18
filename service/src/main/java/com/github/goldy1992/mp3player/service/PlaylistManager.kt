package com.github.goldy1992.mp3player.service

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.github.goldy1992.mp3player.commons.IoDispatcher
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.github.goldy1992.mp3player.service.data.ISavedStateRepository
import com.github.goldy1992.mp3player.service.data.SavedState
import com.github.goldy1992.mp3player.service.library.ContentManager
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.collections4.CollectionUtils
import javax.inject.Inject

@ServiceScoped
class PlaylistManager
    @Inject
    constructor(
        private val contentManager: ContentManager,
        private val savedStateRepository: ISavedStateRepository,
        private val scope : CoroutineScope,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val player : ExoPlayer
    ) : LogTagger, Player.Listener {

    private val savedState = MutableStateFlow(SavedState())

    init {
        Log.i(logTag(), "player: ${player}")
        player.addListener(this)
        scope.launch {
            savedStateRepository.getSavedState()
                .collect {
                    savedState.value = it
                }
        }

    }

    fun loadPlayerState() {
        scope.launch {
            withContext(mainDispatcher) {
                val currentSavedState = savedState.value
                Log.i(logTag(), "player state value: ${currentSavedState}")
                val currentPlaylistIds = currentSavedState.playlist
                val currentTrackPosition = currentSavedState.currentTrackPosition

                val playlist = contentManager.getContentByIds(currentPlaylistIds)
                val currentTrackIndex = currentSavedState.currentTrackIndex

                Log.i(logTag(), "adding to queue")
                player.addMediaItems(playlist)
                player.seekTo(currentTrackIndex, currentTrackPosition)
                Log.i(logTag(), "calling prepare")
                player.prepare()
            }
        }
    }


    private var playlist : MutableList<MediaItem> = mutableListOf()
    private var queueIndex = EMPTY_PLAYLIST_INDEX
    private var playlistId : String? = null

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        Log.i(logTag(), "onMediaMetadataChanged")
        scope.launch(ioDispatcher) {
            var playlist : List<MediaItem>? = null
            var currentMediaId: String? = null
            var currentTrackIndex : Int? = null
            withContext(mainDispatcher) {
                currentMediaId = player.currentMediaItem?.mediaId
                playlist = getPlaylist(player)
                currentTrackIndex = player.currentMediaItemIndex

            }
            if (currentMediaId != null) {
                savedStateRepository.updateCurrentTrack(currentMediaId!!)
                Log.i(logTag(), "updated currentTrack in SaveStateRepository to ${currentMediaId}")
            }
            if (playlist != null) {
                val playlistIds : List<String> = playlist!!.map { item -> item.mediaId }
                savedStateRepository.updatePlaylist(playlistIds)

                Log.i(logTag(), "updated playlist in SaveStateRepository")
            }
            if (currentTrackIndex != null) {
                savedStateRepository.updateCurrentTrackIndex(currentTrackIndex!!)
                Log.i(logTag(), "updated currentTrackIndex in SaveStateRepository to: ${currentTrackIndex}")
            }
        }

    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.i(logTag(), "onIsPlayingChanged: $isPlaying")
        scope.launch(ioDispatcher) {
            var currentTrackPosition : Long?
            withContext(mainDispatcher) {
                currentTrackPosition = player.currentPosition
                Log.i(logTag(), "currentPosition: $currentTrackPosition")

            }
            if (currentTrackPosition != null) {
                savedStateRepository.updateCurrentTrackPosition(currentTrackPosition!!)
                Log.i(logTag(), "updated currentPosition in SaveStateRepository to $currentTrackPosition")
            }
        }
        super.onIsPlayingChanged(isPlaying)
    }
    override fun onPlaybackStateChanged(@Player.State playbackState: Int) {
        Log.i(logTag(), "onPlaybackStateChanged: $playbackState")
        super.onPlaybackStateChanged(playbackState)
    }

    suspend fun saveState() {
        Log.i(logTag(), "Invoked save state")

            val savedState = SavedState(
                playlist = playlist.map { m -> m.mediaId },
                currentTrackIndex = player.currentMediaItemIndex,
                currentTrack = player.currentMediaItem?.mediaId ?: "",
                shuffleEnabled = player.shuffleModeEnabled,
                currentTrackPosition = player.currentPosition,
                repeatMode = player.repeatMode
            )

        Log.i(logTag(), "Created Save State object for saving: ${savedState}")
        savedStateRepository.updateSavedState(savedState)
        Log.i(logTag(), "Saved new Save State object")

    }

    @Suppress("UNCHECKED_CAST")
    fun createNewPlaylist(newList: List<MediaItem?>?): Boolean {
        playlist!!.clear()
        val result = playlist.addAll(newList as List<MediaItem>)
        queueIndex = if (playlist.isEmpty()) EMPTY_PLAYLIST_INDEX else START_OF_PLAYLIST
        return result
    }

    fun getCurrentPlaylist() : List<MediaItem> {
        return playlist.toList()
    }
    @Synchronized
    fun getItemAtIndex(index: Int): MediaItem? {
        return if (validQueueIndex(index)) {
            playlist!![index]
        } else null
    }

    @Synchronized
    fun isEmpty() : Boolean {
        return CollectionUtils.isEmpty(playlist)
    }

    @get:Synchronized
    val currentItem: MediaItem?
        get() = if (validQueueIndex(queueIndex)) {
            playlist!![queueIndex]
        } else null

    @Synchronized
    private fun validQueueIndex(newQueueIndex: Int): Boolean {
        return newQueueIndex < playlist!!.size && newQueueIndex >= 0
    }

    private fun getPlaylist(player: Player) : List<MediaItem> {
        val count = player.mediaItemCount
        val mediaItems = mutableListOf<MediaItem>()

        for (index in 0 until  count) {
            val mediaItem = player.getMediaItemAt(index)
            mediaItems.add(mediaItem)
        }

        return mediaItems.toList()
    }

    companion object {
        private const val START_OF_PLAYLIST = 0
        private const val EMPTY_PLAYLIST_INDEX = -1
    }

    init {
        queueIndex = START_OF_PLAYLIST
    }

    override fun logTag(): String {
        return "PlaylistManager"
    }
}