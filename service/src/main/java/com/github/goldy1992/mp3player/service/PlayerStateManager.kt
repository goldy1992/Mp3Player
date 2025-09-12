package com.github.goldy1992.mp3player.service

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.commons.IoDispatcher
import com.github.goldy1992.mp3player.commons.LoggingUtils
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.ServiceCoroutineScope
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

        @ServiceCoroutineScope
        private val scope : CoroutineScope,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val player : Player
    ) : Player.Listener {

    companion object {
        const val LOG_TAG = "PlayerStateManager"
    }

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
                isInitialised = it
                Log.v(LOG_TAG, "ContentManager isInitialised: $it")
                if (it) {
                    loadPlayerState()
                }
            }
        }

    }

    private suspend fun loadPlayerState() {
        withContext(mainDispatcher) {
            val currentSavedState = savedState.value
            Log.d(LOG_TAG, "loadPlayerState() loading SavedState: $currentSavedState")
            if (isValidSavedState(currentSavedState)) {
                val currentPlaylistIds = currentSavedState.playlist
                val currentTrackPosition = currentSavedState.currentTrackPosition

                val playlist = contentManager.getContentByIds(currentPlaylistIds)
                val currentTrackIndex = currentSavedState.currentTrackIndex

                Log.v(LOG_TAG, "loadPlayerState() Adding SavedState to Player queue.")
                player.addMediaItems(playlist)
                player.seekTo(currentTrackIndex, currentTrackPosition)
                Log.v(LOG_TAG, "loadPlayerState() Calling Player.prepare()")
                player.prepare()
                Log.v(LOG_TAG, "loadPlayerState() Player prepared")
            } else {
                setDefaultPlaylist()
            }
        }
        isInitialised = true

    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        Log.d(LOG_TAG, "onMediaMetadataChanged() invoked with MediaMetaData: $mediaMetadata")
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
                Log.d(LOG_TAG, "onMediaMetadataChanged() updated currentTrack in SaveStateRepository to $currentMediaId")
            }
            if (playlist != null) {
                val playlistIds : List<String> = playlist!!.map { item -> item.mediaId }
                savedStateRepository.updatePlaylist(playlistIds)

                Log.i(LOG_TAG, "onMediaMetadataChanged() updated playlist in SaveStateRepository")
            }
            if (currentTrackIndex != null) {
                savedStateRepository.updateCurrentTrackIndex(currentTrackIndex!!)
                Log.i(LOG_TAG, "onMediaMetadataChanged() updated currentTrackIndex in SaveStateRepository to: $currentTrackIndex")
            }
        }

    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.d(LOG_TAG, "onIsPlayingChanged() invoked with: $isPlaying")
        scope.launch(ioDispatcher) {
            var currentTrackPosition : Long?
            withContext(mainDispatcher) {
                currentTrackPosition = player.currentPosition
                Log.v(LOG_TAG, "onIsPlayingChanged() currentPosition: $currentTrackPosition")

            }
            if (currentTrackPosition != null) {
                savedStateRepository.updateCurrentTrackPosition(currentTrackPosition!!)
                Log.v(LOG_TAG, "onIsPlayingChanged() updated currentPosition in SaveStateRepository to $currentTrackPosition")
            }
        }
        super.onIsPlayingChanged(isPlaying)
    }

    fun saveState() {
        Log.v(LOG_TAG, "saveState() invoked. isInitialised: $isInitialised")
        if (isInitialised) {
            val savedState: SavedState?
            val playbackState = player.playbackState
            Log.d(LOG_TAG, "saveState() currentPlaybackState: ${LoggingUtils.logPlaybackState(playbackState, LOG_TAG)}")
            val currentPlaylist = getPlaylist(player)
            val currentPlaylistIdList = currentPlaylist.map { m -> m.mediaId }
            Log.d(LOG_TAG, "saveState() got current playlist list: ${currentPlaylistIdList.joinToString(",")}")
            val currentMediaItemIndex = player.currentMediaItemIndex
            Log.d(LOG_TAG, "saveState() currentMediaItemIndex: $currentMediaItemIndex")
            val currentTrack = player.currentMediaItem?.mediaId ?: ""
            Log.d(LOG_TAG, "saveState() currentTrack: $currentTrack")
            val shuffleEnabled = player.shuffleModeEnabled
            Log.d(LOG_TAG, "saveState() shuffle enabled: $shuffleEnabled")
            val currentPosition = player.currentPosition
            Log.d(LOG_TAG, "saveState() currentPosition: $currentPosition")
            val repeatMode = player.repeatMode
            Log.d(LOG_TAG, "saveState() repeatMode: $repeatMode")
            savedState = SavedState(
                playlist = currentPlaylistIdList,
                currentTrackIndex = currentMediaItemIndex,
                currentTrack = currentTrack,
                shuffleEnabled = shuffleEnabled,
                currentTrackPosition = currentPosition,
                repeatMode = repeatMode
            )
            Log.d(LOG_TAG, "saveState() created SaveState object for saving: $savedState")
            scope.launch(mainDispatcher) {
                savedStateRepository.updateSavedState(savedState)
                Log.i(LOG_TAG, "saveState() saved new SaveState object $savedState")
            }
        } else {
            Log.w(LOG_TAG, "saveState() PlayerStateManager not initialised, not saving")
        }

    }

    private fun getPlaylist(player: Player) : List<MediaItem> {
        Log.v(LOG_TAG, "getPlaylist() invoked from player: $player")
        val count = player.mediaItemCount
        Log.d(LOG_TAG, "getPlaylist() mediaItemCount: $count")
        val mediaItems = mutableListOf<MediaItem>()

        for (index in 0 until  count) {
            val mediaItem = player.getMediaItemAt(index)
            mediaItems.add(mediaItem)
        }
        Log.v(LOG_TAG, "playlist retrieved, size: ${mediaItems.size}")
        return mediaItems.toList()
    }

    private fun isValidSavedState(savedState : SavedState) : Boolean {
        Log.v(LOG_TAG, "isValidSavedState() invoked with SavedState $savedState")
        val playlistSize = savedState.playlist.size
        val validPlaylist = playlistSize > 0

        val currentTrackIndex = savedState.currentTrackIndex
        val validCurrentTrackIndex = currentTrackIndex in 0 until playlistSize
        val isValid = validPlaylist && validCurrentTrackIndex
        Log.d(LOG_TAG, "isValidSavedState() playlistSize: $playlistSize, currentTrackIndex: $currentTrackIndex, isValid = $isValid")
        return isValid
    }

    private suspend fun setDefaultPlaylist() {
        Log.v(LOG_TAG, "setDefaultPlaylist() invoked")
        val contentManagerResult = contentManager.getChildren(MediaItemType.SONGS)
        val defaultPlaylist = contentManagerResult.children
        if (isNotEmpty(defaultPlaylist)) {
            Log.d(LOG_TAG, "setDefaultPlaylist() adding to queue default playlist: ${defaultPlaylist.size}")
            player.addMediaItems(defaultPlaylist)
            player.seekTo(0, 0)
            Log.v(LOG_TAG, "setDefaultPlaylist() calling Player.prepare()")
            player.prepare()
        } else {
            Log.d(LOG_TAG, "setDefaultPlaylist() default playlist is empty")
        }
    }

}