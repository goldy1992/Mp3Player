package com.github.goldy1992.mp3player.client.repositories.preferences

import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferences
import com.github.goldy1992.mp3player.client.ui.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FakeUserPreferencesRepository
    @Inject
    constructor()
    : IUserPreferencesRepository {
    val theme = MutableStateFlow(Theme.BLUE)

    val userPreferences = MutableStateFlow(UserPreferences.DEFAULT)
    override fun userPreferencesFlow(): Flow<UserPreferences> {
        return userPreferences
    }

    override suspend fun updateTheme(newTheme: Theme) {
        theme.value = newTheme
    }


    val darkMode = MutableStateFlow(false)
    override suspend fun updateDarkMode(useDarkMode: Boolean) {
        darkMode.value = useDarkMode
    }


    val useSystemDarkMode = MutableStateFlow(true)
    override suspend fun updateSystemDarkMode(useSystemDarkMode: Boolean) {
        this.useSystemDarkMode.value = useSystemDarkMode
    }



    override suspend fun updateUseDynamicColor(useDynamicColor: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun updateLanguage(language: String) {
        TODO("Not yet implemented")
    }

}