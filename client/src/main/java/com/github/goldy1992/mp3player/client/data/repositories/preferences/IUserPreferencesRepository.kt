package com.github.goldy1992.mp3player.client.data.repositories.preferences

import com.github.goldy1992.mp3player.client.ui.Theme
import kotlinx.coroutines.flow.Flow

interface IUserPreferencesRepository {

    fun userPreferencesFlow() : Flow<UserPreferences>

    suspend fun updateTheme(newTheme : Theme)

    suspend fun updateDarkMode(useDarkMode : Boolean)

    suspend fun updateSystemDarkMode(useSystemDarkMode : Boolean)

    suspend fun updateUseDynamicColor(useDynamicColor : Boolean)

    suspend fun updateLanguage(language : String)

}