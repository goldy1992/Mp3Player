package com.github.goldy1992.mp3player.client.data.repositories.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.github.goldy1992.mp3player.client.ui.Theme
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


data class UserPreferences(
    val darkMode: Boolean,
    val systemDarkMode : Boolean,
    val theme: String
)  {
    companion object {
        val DEFAULT = UserPreferences(darkMode = false, systemDarkMode = false, theme = "None")
    }
}

/**
 * Class that handles saving and retrieving user preferences
 */
open class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) : IUserPreferencesRepository, LogTagger {

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("theme")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val USE_SYSTEM_DARK_MODE = booleanPreferencesKey("system_dark_mode")
    }

    /**
     * Get the user preferences flow.
     */
    private val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(logTag(), "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences : Preferences ->
            // Get the sort order from preferences and convert it to a [SortOrder] object
            val themeString : String? = preferences[PreferencesKeys.THEME]
            val theme : Theme = if (themeString != null) Theme.valueOf(themeString) else Theme.BLUE

            // Get our show completed value, defaulting to false if not set:
            val darkMode : Boolean = preferences[PreferencesKeys.DARK_MODE] ?: false
            // default to System dark mode if no preferences are stored!
            val systemDarkMode : Boolean = preferences[PreferencesKeys.USE_SYSTEM_DARK_MODE] ?: true
            UserPreferences(darkMode, systemDarkMode, theme.name)
        }

    override suspend fun updateTheme(newTheme: Theme) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = newTheme.name
        }
    }

    override suspend fun updateDarkMode(darkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = darkMode
        }
    }

    override suspend fun updateSystemDarkMode(useSystemDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_SYSTEM_DARK_MODE] = useSystemDarkMode
        }
    }

    override open fun getTheme() : Flow<Theme> {
        return userPreferencesFlow.map { preferences ->
            val currentTheme = preferences.theme
            Theme.valueOf(currentTheme)
        }
    }

    override open fun getDarkMode() : Flow<Boolean> {
       return userPreferencesFlow.map { preferences ->
           preferences.darkMode
        }
    }

    override open fun getSystemDarkMode() : Flow<Boolean> {
        return userPreferencesFlow.map {
            preferences -> preferences.systemDarkMode
        }
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "USER_PREFS_REPO"
    }
}
