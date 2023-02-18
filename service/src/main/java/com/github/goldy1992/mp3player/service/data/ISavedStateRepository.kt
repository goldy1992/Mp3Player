package com.github.goldy1992.mp3player.service.data

import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.flow.Flow

interface ISavedStateRepository : LogTagger {

    fun getSavedState() : Flow<SavedState>
    suspend fun updateSavedState(savedState: SavedState)

    fun getPlaylist() : Flow<List<String>>
    suspend fun updatePlaylist(playlist: List<String>)

    fun getCurrentTrack() : Flow<String>
    suspend fun updateCurrentTrack(currentTrack : String)

    fun getCurrentTrackIndex() : Flow<Int>
    suspend fun updateCurrentTrackIndex(currentTrackIndex : Int)

    fun getCurrentTrackPosition() : Flow<Long>
    suspend fun updateCurrentTrackPosition(position : Long)
}