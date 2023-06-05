package com.github.goldy1992.mp3player.client.data.repositories.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.goldy1992.mp3player.client.ui.Theme
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


data class UserPreferences(
    val darkMode: Boolean = true,
    val systemDarkMode : Boolean = false,
    val theme: String = "None",
    val useDynamicColor: Boolean = false
)  {
    companion object {
        val DEFAULT = UserPreferences(darkMode = false, systemDarkMode = false, theme = "None", useDynamicColor = true)
    }
}

/**
 * Class that handles saving and retrieving user preferences
 */
@Singleton
open class UserPreferencesRepository
    @Inject
    constructor(private val dataStore: DataStore<Preferences>) : IUserPreferencesRepository, LogTagger {

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("theme")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val USE_SYSTEM_DARK_MODE = booleanPreferencesKey("system_dark_mode")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
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
            //
            val useDynamicColor : Boolean = preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: true
            UserPreferences(darkMode, systemDarkMode, theme.name, useDynamicColor)
        }

    override fun userPreferencesFlow(): Flow<UserPreferences> {
        return userPreferencesFlow
    }

    override suspend fun updateTheme(newTheme: Theme) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = newTheme.name
        }
    }

    override suspend fun updateDarkMode(useDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = useDarkMode
        }
    }

    override suspend fun updateSystemDarkMode(useSystemDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_SYSTEM_DARK_MODE] = useSystemDarkMode
        }
    }

    override suspend fun updateUseDynamicColor(useDynamicColor: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_DYNAMIC_COLOR] = useDynamicColor
        }
    }

    override fun getTheme() : Flow<Theme> {
        return userPreferencesFlow.map { preferences ->
            val currentTheme = preferences.theme
            Theme.valueOf(currentTheme)
        }
    }

    override fun getDarkMode() : Flow<Boolean> {
       return userPreferencesFlow.map { preferences ->
           preferences.darkMode
        }
    }

    override fun getSystemDarkMode() : Flow<Boolean> {
        return userPreferencesFlow.map {
            preferences -> preferences.systemDarkMode
        }
    }

    override fun getUseDynamicColor(): Flow<Boolean> {
        return userPreferencesFlow.map {
                preferences -> preferences.useDynamicColor
        }
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "USER_PREFS_REPO"
    }
}
