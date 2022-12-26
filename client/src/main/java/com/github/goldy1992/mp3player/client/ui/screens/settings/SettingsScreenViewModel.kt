package com.github.goldy1992.mp3player.client.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.Theme
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel

    @Inject
    constructor(
        private val userPreferencesRepository: IUserPreferencesRepository
    ) : ViewModel(), LogTagger {

    private val _setting = MutableStateFlow(Settings())
    val settings : StateFlow<Settings> = _setting

    private val _darkMode = MutableStateFlow(false)
    val darkMode : StateFlow<Boolean> = _darkMode
    init {
        viewModelScope.launch {
            userPreferencesRepository.getDarkMode()
                .collect {
                    _darkMode.value = it
                    _setting.value = Settings(darkMode = it,
                                            useSystemDarkMode = settings.value.useSystemDarkMode,
                                            theme = settings.value.theme)
                }
        }
    }

    private val _useSystemDarkMode = MutableStateFlow(true)
    val useSystemDarkMode : StateFlow<Boolean> = _useSystemDarkMode
    init {
        viewModelScope.launch {
            userPreferencesRepository.getSystemDarkMode()
                .collect {
                    _useSystemDarkMode.value = it
                    _setting.value = Settings(darkMode = settings.value.darkMode,
                        useSystemDarkMode = it,
                        theme = settings.value.theme)
                }
        }
    }

    private val _theme = MutableStateFlow(Theme.BLUE)
    val theme : StateFlow<Theme> = _theme
    init {
        viewModelScope.launch {
            userPreferencesRepository.getTheme()
                .collect {
                    _theme.value = it
                    _setting.value = Settings(
                        darkMode = settings.value.darkMode,
                        useSystemDarkMode = settings.value.useSystemDarkMode,
                        theme = it
                    )
                }
        }
    }

    fun setDarkMode(useDarkMode : Boolean) {
        viewModelScope.launch { userPreferencesRepository.updateDarkMode(useDarkMode = useDarkMode) }
    }

    fun setUseSystemDarkMode(useSystemDarkMode : Boolean) {
        viewModelScope.launch { userPreferencesRepository.updateSystemDarkMode(useSystemDarkMode = useSystemDarkMode) }
    }

    fun setTheme(theme : Theme) {
        viewModelScope.launch { userPreferencesRepository.updateTheme(theme) }
    }


    override fun logTag(): String {
        return "SettingsScrnViewModel"
    }

}