package com.github.goldy1992.mp3player.service

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
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
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
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
        private val player : Player
    ) : LogTagger, Player.Listener {

    private val savedState = MutableStateFlow(SavedState())
    private var isInitialised : Boolean = false

    init {
        player.addListener(this)
        scope.launch {
            savedStateRepository.getSavedState()
                .collect {
                    savedState.value = it
                }
        }
        scope.launch {
            contentManager.isInitialised.collect {
                Log.v(logTag(), "ContentManager isInitialised: $it")
                if (it) {
                    loadPlayerState()
                }
            }
        }

    }

    private suspend fun loadPlayerState() {
        withContext(mainDispatcher) {
            val currentSavedState = savedState.value
            Log.d(logTag(), "loadPlayerState() loading SavedState: $currentSavedState")
            if (isValidSavedState(currentSavedState)) {
                val currentPlaylistIds = currentSavedState.playlist
                val currentTrackPosition = currentSavedState.currentTrackPosition

                val playlist = contentManager.getContentByIds(currentPlaylistIds)
                val currentTrackIndex = currentSavedState.currentTrackIndex

                Log.v(logTag(), "loadPlayerState() Adding SavedState to Player queue.")
                player.addMediaItems(playlist)
                player.seekTo(currentTrackIndex, currentTrackPosition)
                Log.v(logTag(), "loadPlayerState() Calling Player.prepare()")
                player.prepare()
                Log.v(logTag(), "loadPlayerState() Player prepared")
            } else {
                setDefaultPlaylist()
            }
        }
        isInitialised = true

    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        Log.d(logTag(), "onMediaMetadataChanged() invoked with MediaMetaData: $mediaMetadata")
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
                Log.d(logTag(), "onMediaMetadataChanged() updated currentTrack in SaveStateRepository to $currentMediaId")
            }
            if (playlist != null) {
                val playlistIds : List<String> = playlist!!.map { item -> item.mediaId }
                savedStateRepository.updatePlaylist(playlistIds)

                Log.i(logTag(), "onMediaMetadataChanged() updated playlist in SaveStateRepository")
            }
            if (currentTrackIndex != null) {
                savedStateRepository.updateCurrentTrackIndex(currentTrackIndex!!)
                Log.i(logTag(), "onMediaMetadataChanged() updated currentTrackIndex in SaveStateRepository to: $currentTrackIndex")
            }
        }

    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.d(logTag(), "onIsPlayingChanged() invoked with: $isPlaying")
        scope.launch(ioDispatcher) {
            var currentTrackPosition : Long?
            withContext(mainDispatcher) {
                currentTrackPosition = player.currentPosition
                Log.v(logTag(), "onIsPlayingChanged() currentPosition: $currentTrackPosition")

            }
            if (currentTrackPosition != null) {
                savedStateRepository.updateCurrentTrackPosition(currentTrackPosition!!)
                Log.v(logTag(), "onIsPlayingChanged() updated currentPosition in SaveStateRepository to $currentTrackPosition")
            }
        }
        super.onIsPlayingChanged(isPlaying)
    }

    fun saveState() {
        Log.v(logTag(), "saveState() invoked. isInitialised: $isInitialised")
        if (isInitialised) {
            val savedState: SavedState?
            val playbackState = player.playbackState
            val playbackStateStr: String = playbackStateDebugMap[playbackState] ?: "UNKNOWN_STATE"
            Log.d(logTag(), "saveState() currentPlaybackState: $playbackStateStr")
            val currentPlaylist = getPlaylist(player)
            val currentPlaylistIdList = currentPlaylist.map { m -> m.mediaId }
            Log.d(logTag(), "saveState() got current playlist list: ${currentPlaylistIdList.joinToString(",")}")
            val currentMediaItemIndex = player.currentMediaItemIndex
            Log.d(logTag(), "saveState() currentMediaItemIndex: $currentMediaItemIndex")
            val currentTrack = player.currentMediaItem?.mediaId ?: ""
            Log.d(logTag(), "saveState() currentTrack: $currentTrack")
            val shuffleEnabled = player.shuffleModeEnabled
            Log.d(logTag(), "saveState() shuffle enabled: $shuffleEnabled")
            val currentPosition = player.currentPosition
            Log.d(logTag(), "saveState() currentPosition: $currentPosition")
            val repeatMode = player.repeatMode
            Log.d(logTag(), "saveState() repeatMode: $repeatMode")
            savedState = SavedState(
                playlist = currentPlaylistIdList,
                currentTrackIndex = currentMediaItemIndex,
                currentTrack = currentTrack,
                shuffleEnabled = shuffleEnabled,
                currentTrackPosition = currentPosition,
                repeatMode = repeatMode
            )
            Log.d(logTag(), "saveState() created SaveState object for saving: $savedState")
            scope.launch(ioDispatcher) {
                savedStateRepository.updateSavedState(savedState)
                Log.i(logTag(), "saveState() saved new SaveState object $savedState")
            }
        } else {
            Log.w(logTag(), "saveState() PlayerStateManager not initialised, not saving")
        }

    }

    private fun getPlaylist(player: Player) : List<MediaItem> {
        Log.v(logTag(), "getPlaylist() invoked from player: $player")
        val count = player.mediaItemCount
        Log.d(logTag(), "getPlaylist() mediaItemCount: $count")
        val mediaItems = mutableListOf<MediaItem>()

        for (index in 0 until  count) {
            val mediaItem = player.getMediaItemAt(index)
            mediaItems.add(mediaItem)
        }
        Log.v(logTag(), "playlist retrieved, size: ${mediaItems.size}")
        return mediaItems.toList()
    }

    private fun isValidSavedState(savedState : SavedState) : Boolean {
        Log.v(logTag(), "isValidSavedState() invoked with SavedState $savedState")
        val playlistSize = savedState.playlist.size
        val validPlaylist = playlistSize > 0

        val currentTrackIndex = savedState.currentTrackIndex
        val validCurrentTrackIndex = currentTrackIndex in 0 until playlistSize
        val isValid = validPlaylist && validCurrentTrackIndex
        Log.d(logTag(), "isValidSavedState() playlistSize: $playlistSize, currentTrackIndex: $currentTrackIndex, isValid = $isValid")
        return isValid
    }

    private suspend fun setDefaultPlaylist() {
        Log.v(logTag(), "setDefaultPlaylist() invoked")
        val contentManagerResult = contentManager.getChildren(MediaItemType.SONGS)
        val defaultPlaylist = contentManagerResult.children
        if (isNotEmpty(defaultPlaylist)) {
            Log.d(logTag(), "setDefaultPlaylist() adding to queue default playlist: ${defaultPlaylist.size}")
            player.addMediaItems(defaultPlaylist)
            player.seekTo(0, 0)
            Log.v(logTag(), "setDefaultPlaylist() calling Player.prepare()")
            player.prepare()
        } else {
            Log.d(logTag(), "setDefaultPlaylist() default playlist is empty")
        }
    }

    override fun logTag(): String {
        return "PlayerStateManager"
    }
}