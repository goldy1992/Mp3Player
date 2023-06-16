package com.github.goldy1992.mp3player.client.data.repositories.preferences

import com.github.goldy1992.mp3player.client.ui.Theme
import kotlinx.coroutines.flow.Flow

interface IUserPreferencesRepository {

    fun userPreferencesFlow() : Flow<UserPreferences>

    suspend fun updateTheme(newTheme : Theme)
    fun getTheme() : Flow<Theme>

    suspend fun updateDarkMode(useDarkMode : Boolean)
    fun getDarkMode() : Flow<Boolean>

    suspend fun updateSystemDarkMode(useSystemDarkMode : Boolean)
    fun getSystemDarkMode() : Flow<Boolean>

    suspend fun updateUseDynamicColor(useDynamicColor : Boolean)
    fun getUseDynamicColor() : Flow<Boolean>

}