package com.github.goldy1992.mp3player.service

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.commons.Constants.playbackStateDebugMap
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
                    Log.i(logTag(), "Player prepared")
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

    override fun onPlaybackStateChanged(playbackState: Int) {
        Log.i(logTag(), "onPlaybackStateChanged called")
        LoggingUtils.logPlaybackState(playbackState, logTag())
        super.onPlaybackStateChanged(playbackState)
    }

    fun saveState() {
        var savedState : SavedState?
        Log.i(logTag(), "Invoked save state")
        val playbackState = player.playbackState
        val playbackStateStr : String = playbackStateDebugMap[playbackState] ?: "UNKNOWN_STATE"
        Log.i(logTag(), "currentPlaybackState: $playbackStateStr")
        if (player != null) {
            val playlist = getPlaylist(player)
            val playlistIdList = playlist.map { m -> m.mediaId }
            Log.i(logTag(), "got playlist id list")
            val currentMediaItemIndex = player.currentMediaItemIndex
            Log.i(logTag(), "currentMediaItemIndex: $currentMediaItemIndex")
            val currentTrack = player.currentMediaItem?.mediaId ?: ""
            Log.i(logTag(), "currentTrack: $currentTrack")
            val shuffleEnabled = player.shuffleModeEnabled
            Log.i(logTag(), "shuffle enabled: $shuffleEnabled")
            val currentPosition = player.currentPosition
            Log.i(logTag(), "currentPosition: $currentPosition")
            val repeatMode = player.repeatMode
            Log.i(logTag(), "repeatMode: $repeatMode")
            savedState = SavedState(
                playlist = playlistIdList,
                currentTrackIndex = currentMediaItemIndex,
                currentTrack = currentTrack,
                shuffleEnabled = shuffleEnabled,
                currentTrackPosition = currentPosition,
                repeatMode = repeatMode
            )
            Log.i(logTag(), "Created Save State object for saving: $savedState")
            scope.launch(ioDispatcher) {
                savedStateRepository.updateSavedState(savedState)
                Log.i(logTag(), "Saved new Save State object")
            }
        } else {
            Log.w(logTag(), "Error getting state to save, NOT saving!")
        }

    }

    private fun getPlaylist(player: Player) : List<MediaItem> {
        Log.i(logTag(), "getting playlist from player: $player")
        val count = player.mediaItemCount
        Log.i(logTag(), "mediaItemCount: $count")
        val mediaItems = mutableListOf<MediaItem>()

        for (index in 0 until  count) {
            val mediaItem = player.getMediaItemAt(index)
            mediaItems.add(mediaItem)
        }
        Log.i(logTag(), "playlist retrieved, size: ${mediaItems.size}")
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