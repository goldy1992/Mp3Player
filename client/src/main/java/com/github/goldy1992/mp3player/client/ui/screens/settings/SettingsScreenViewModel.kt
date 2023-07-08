package com.github.goldy1992.mp3player.client.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.Theme
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.data.repositories.permissions.IPermissionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [SettingsScreen]
 */
@HiltViewModel
class SettingsScreenViewModel

    @Inject
    constructor(
        private val userPreferencesRepository: IUserPreferencesRepository,
        private val permissionsRepository: IPermissionsRepository
    ) : ViewModel(), LogTagger {

    private val _permissionsState : MutableStateFlow<Map<String, Boolean>> = MutableStateFlow(
        emptyMap()
    )
    val permissionState : StateFlow<Map<String, Boolean>> = _permissionsState
    init {
        viewModelScope.launch { permissionsRepository.permissionsFlow()
            .collect {
                _permissionsState.value = it
            }}
    }

    private val _setting = MutableStateFlow(Settings())
    val settings : StateFlow<Settings> = _setting

    init {
        viewModelScope.launch {
            userPreferencesRepository
                .userPreferencesFlow()
                .collect {
                    _setting.value = Settings(
                        darkMode = it.darkMode,
                        useSystemDarkMode = it.systemDarkMode,
                        //theme = Theme(it.theme),
                        dynamicColor = it.useDynamicColor,
                        language = it.language
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

    fun setUseDynamicColor(useDynamicColor : Boolean) {
        viewModelScope.launch { userPreferencesRepository.updateUseDynamicColor(useDynamicColor) }
    }

    fun setLanguage(language : String) {
        Log.d(logTag(), "Updating language to $language")
        viewModelScope.launch { userPreferencesRepository.updateLanguage(language) }
    }


    override fun logTag(): String {
        return "SettingsScreenViewModel"
    }

}