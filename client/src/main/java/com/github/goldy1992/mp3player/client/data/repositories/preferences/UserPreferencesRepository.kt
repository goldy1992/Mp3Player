package com.github.goldy1992.mp3player.client.data.repositories.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


data class UserPreferences(
    val darkMode: Boolean = true,
    val systemDarkMode : Boolean = false,
    val useDynamicColor: Boolean = false,
    val language : String = "en"
)  {
    companion object {
        val DEFAULT = UserPreferences(
            darkMode = false,
            systemDarkMode = false,
            useDynamicColor = true,
            language = "en"
        )
    }
}

/**
 * Class that handles saving and retrieving user preferences
 */
@Singleton
open class UserPreferencesRepository
    @Inject
    constructor(private val dataStore: DataStore<Preferences>) : IUserPreferencesRepository {


    companion object {
        const val LOG_TAG = "UserPreferencesRepository"
    }

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("theme")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val USE_SYSTEM_DARK_MODE = booleanPreferencesKey("system_dark_mode")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val LANGUAGE = stringPreferencesKey("language")
    }

    /**
     * Get the user preferences flow.
     */
    private val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(LOG_TAG, "userPreferencesFlow: IOException reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences : Preferences ->
            // Get the sort order from preferences and convert it to a [SortOrder] object

            // Get our show completed value, defaulting to false if not set:
            val darkMode : Boolean = preferences[PreferencesKeys.DARK_MODE] ?: false
            // default to System dark mode if no preferences are stored!
            val systemDarkMode : Boolean = preferences[PreferencesKeys.USE_SYSTEM_DARK_MODE] ?: true
            val useDynamicColor : Boolean = preferences[PreferencesKeys.USE_DYNAMIC_COLOR] ?: true
            val language : String = preferences[PreferencesKeys.LANGUAGE] ?: "en"
            val userPreferences = UserPreferences(darkMode, systemDarkMode, useDynamicColor, language)

            Log.d(LOG_TAG, "userPreferencesFlow: preferences mapped to $userPreferences")
            userPreferences
        }

    override fun userPreferencesFlow(): Flow<UserPreferences> {
        return userPreferencesFlow
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

    override suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }

    /**
     * @return the name of the log tag given to the class
     */
}
