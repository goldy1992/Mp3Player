package com.github.goldy1992.mp3player.service

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.github.goldy1992.mp3player.commons.IoDispatcher
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.data.ISavedStateRepository
import com.github.goldy1992.mp3player.service.data.SavedState
import com.github.goldy1992.mp3player.service.library.ContentManager
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Manages saving the playlist state to the Apps [DataStore].
 */
@ServiceScoped
class PlayerStateManager
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
                if (isValidSavedState(currentSavedState)) {
                    val currentPlaylistIds = currentSavedState.playlist
                    val currentTrackPosition = currentSavedState.currentTrackPosition

                    val playlist = contentManager.getContentByIds(currentPlaylistIds)
                    val currentTrackIndex = currentSavedState.currentTrackIndex

                    Log.i(logTag(), "adding to queue")
                    player.addMediaItems(playlist)
                    player.seekTo(currentTrackIndex, currentTrackPosition)
                    Log.i(logTag(), "calling prepare")
                    player.prepare()
                } else {
                    setDefaultPlaylist()
                }
            }
        }
    }

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

    suspend fun saveState() {
        var savedState : SavedState?
        withContext(mainDispatcher) {
            Log.i(logTag(), "Invoked save state")
            val playlist = getPlaylist(player)
             savedState = SavedState(
                playlist = playlist.map { m -> m.mediaId },
                currentTrackIndex = player.currentMediaItemIndex,
                currentTrack = player.currentMediaItem?.mediaId ?: "",
                shuffleEnabled = player.shuffleModeEnabled,
                currentTrackPosition = player.currentPosition,
                repeatMode = player.repeatMode
            )
        }

        Log.i(logTag(), "Created Save State object for saving: $savedState")
        if (savedState == null) {
            Log.w(logTag(), "Error getting state to save, NOT saving!")
        } else {
            savedStateRepository.updateSavedState(savedState!!)
            Log.i(logTag(), "Saved new Save State object")
        }

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

    private fun isValidSavedState(savedState : SavedState) : Boolean {
        val playlistSize = savedState.playlist.size
        val validPlaylist = playlistSize > 0

        val currentTrackIndex = savedState.currentTrackIndex
        val validCurrentTrackIndex = currentTrackIndex in 0 until playlistSize
        val isValid = validPlaylist && validCurrentTrackIndex
        Log.i(logTag(), "playlistSize: $playlistSize, currentTrackIndex: $currentTrackIndex, isValid = $isValid")
        return isValid
    }

    private suspend fun setDefaultPlaylist() {
        Log.i(logTag(), "Setting default playlist")
        val defaultPlaylist = contentManager.getChildren(MediaItemType.SONGS)
        Log.i(logTag(), "adding to queue")
        player.addMediaItems(defaultPlaylist)
        player.seekTo(0, 0)
        Log.i(logTag(), "calling prepare")
        player.prepare()
    }

    companion object {
        private const val START_OF_PLAYLIST = 0
        private const val EMPTY_PLAYLIST_INDEX = -1
    }

    override fun logTag(): String {
        return "PlayerStateManager"
    }
}