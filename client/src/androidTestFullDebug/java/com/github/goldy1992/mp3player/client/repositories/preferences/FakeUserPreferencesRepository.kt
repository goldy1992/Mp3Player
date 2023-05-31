package com.github.goldy1992.mp3player.client.repositories.preferences

import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferences
import com.github.goldy1992.mp3player.client.ui.Theme
import com.github.goldy1992.mp3player.client.ui.screens.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
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

    override fun getTheme(): Flow<Theme> {
        return theme
    }

    val darkMode = MutableStateFlow(false)
    override suspend fun updateDarkMode(useDarkMode: Boolean) {
        darkMode.value = useDarkMode
    }

    override fun getDarkMode(): Flow<Boolean> {
        return darkMode
    }

    val useSystemDarkMode = MutableStateFlow(true)
    override suspend fun updateSystemDarkMode(useSystemDarkMode: Boolean) {
        this.useSystemDarkMode.value = useSystemDarkMode
    }

    override fun getSystemDarkMode(): Flow<Boolean> {
        return useSystemDarkMode
    }

    override suspend fun updateUseDynamicColor(useDynamicColor: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getUseDynamicColor(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}