package com.github.goldy1992.mp3player.service.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.media3.common.Player
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.apache.commons.lang3.StringUtils.isNotEmpty
import java.io.IOException
import javax.inject.Inject

@ServiceScoped
open class DefaultSavedStateRepository
    @Inject
    constructor(private val dataStore : DataStore<Preferences>) : ISavedStateRepository {

    private object PreferencesKeys {
        val PLAYLIST = stringPreferencesKey("playlist")
        val CURRENT_TRACK = stringPreferencesKey("current_track")
        val CURRENT_TRACK_INDEX = intPreferencesKey("current_track_index")
        val CURRENT_TRACK_POSITION = longPreferencesKey("current_track_position")
    }

    /**
     * Get the user preferences flow.
     */
    private val savedStateFlow: Flow<SavedState> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(logTag(), "savedStateFlow Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
                preferences : Preferences -> preferencesToSavedState(preferences)
        }


    override suspend fun updatePlaylist(playlist: List<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PLAYLIST] = playlist.joinToString(",")
        }
    }

    override suspend fun updateCurrentTrackIndex(currentTrackIndex: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_TRACK_INDEX] = currentTrackIndex
        }
    }

    override suspend fun updateCurrentTrack(currentTrack: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_TRACK] = currentTrack
        }
    }

    override suspend fun updateCurrentTrackPosition(position : Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENT_TRACK_POSITION] = position
        }
    }

    override fun getSavedState(): Flow<SavedState> {
        return savedStateFlow
    }

    override suspend fun updateSavedState(savedState: SavedState) {
        dataStore.edit { preferences ->
            Log.v(logTag(), "updateSavedState() datastore.edit invoked")
            preferences[PreferencesKeys.PLAYLIST] = savedState.playlist.joinToString(",")
            preferences[PreferencesKeys.CURRENT_TRACK] = savedState.currentTrack
            preferences[PreferencesKeys.CURRENT_TRACK_POSITION] = savedState.currentTrackPosition
            preferences[PreferencesKeys.CURRENT_TRACK_POSITION] = savedState.currentTrackPosition
            Log.v(logTag(), "updateSavedState() datastore.edit invocation complete")
        }
    }

    override fun getPlaylist(): Flow<List<String>> {
        return savedStateFlow.map { preferences -> preferences.playlist }
    }

    override fun getCurrentTrack() : Flow<String> {
        return savedStateFlow.map { preferences -> preferences.currentTrack }
    }


    override fun getCurrentTrackIndex() : Flow<Int> {
        return savedStateFlow.map { preferences -> preferences.currentTrackIndex }
    }

    override fun getCurrentTrackPosition(): Flow<Long> {
        return savedStateFlow.map { preferences -> preferences.currentTrackPosition }
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "DefaultSavedtateRepo"
    }

    private fun preferencesToSavedState(preferences : Preferences) : SavedState {
        val playlistString : String? = preferences[PreferencesKeys.PLAYLIST]

        val playlist : MutableList<String> = mutableListOf()
        if (isNotEmpty(playlistString)) {
            playlist.addAll(playlistString?.split(",") ?: emptyList())
        }

        val currentTrack : String = preferences[PreferencesKeys.CURRENT_TRACK] ?: ""

        val currentTrackIndex : Int = preferences[PreferencesKeys.CURRENT_TRACK_INDEX] ?: -1

        val currentTrackPosition : Long = preferences[PreferencesKeys.CURRENT_TRACK_POSITION] ?: 0L
        Log.d(logTag(), "preferencesToSavedState() Mapping preferences to saved state :- currentTrack: $currentTrack, currentTrackIndex: $currentTrackIndex, currentTrackPosition: $currentTrackPosition")
        return SavedState(
            playlist = playlist,
            currentTrack = currentTrack,
            currentTrackPosition = currentTrackPosition,
            currentTrackIndex = currentTrackIndex
        )
    }
}

data class SavedState(
    val playlist: List<String> = emptyList(),
    val currentTrack : String = "",
    val currentTrackIndex : Int = 0,
    val currentTrackPosition : Long = 0L,
    val shuffleEnabled : Boolean = false,
    val repeatMode : Int = Player.REPEAT_MODE_OFF
)  {
    companion object {
        val DEFAULT = SavedState()
    }
}