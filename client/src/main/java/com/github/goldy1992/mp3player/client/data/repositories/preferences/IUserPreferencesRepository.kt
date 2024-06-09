package com.github.goldy1992.mp3player.client.data.repositories.preferences

import kotlinx.coroutines.flow.Flow

interface IUserPreferencesRepository {

    fun userPreferencesFlow() : Flow<UserPreferences>

    suspend fun updateDarkMode(useDarkMode : Boolean)

    suspend fun updateSystemDarkMode(useSystemDarkMode : Boolean)

    suspend fun updateUseDynamicColor(useDynamicColor : Boolean)

    suspend fun updateLanguage(language : String)

}