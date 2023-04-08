package com.github.goldy1992.mp3player.client.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.Theme
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.data.repositories.permissions.IPermissionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                        dynamicColor = it.useDynamicColor
                    )
                }
        }
    }

//    private val _darkMode = MutableStateFlow(false)
//    val darkMode : StateFlow<Boolean> = _darkMode
//    init {
//        viewModelScope.launch {
//            userPreferencesRepository.getDarkMode()
//                .collect {
//                    _darkMode.value = it
//                    _setting.value = Settings(darkMode = it,
//                                            useSystemDarkMode = settings.value.useSystemDarkMode,
//                                            theme = settings.value.theme,
//                                            dynamicColor = settings.value.dynamicColor)
//                }
//        }
//    }

//    private val _useSystemDarkMode = MutableStateFlow(true)
//    val useSystemDarkMode : StateFlow<Boolean> = _useSystemDarkMode
//    init {
//        viewModelScope.launch {
//            userPreferencesRepository.getSystemDarkMode()
//                .collect {
//                    _useSystemDarkMode.value = it
//                    _setting.value = Settings(darkMode = settings.value.darkMode,
//                        useSystemDarkMode = it,
//                        theme = settings.value.theme,
//                        dynamicColor = settings.value.dynamicColor)
//                }
//        }
//    }
//
//    private val _theme = MutableStateFlow(Theme.BLUE)
//    val theme : StateFlow<Theme> = _theme
//    init {
//        viewModelScope.launch {
//            userPreferencesRepository.getTheme()
//                .collect {
//                    _theme.value = it
//                    _setting.value = Settings(
//                        darkMode = settings.value.darkMode,
//                        useSystemDarkMode = settings.value.useSystemDarkMode,
//                        theme = it,
//                        dynamicColor = settings.value.dynamicColor
//                    )
//                }
//        }
//    }
//
//    private val _useDynamicColor = MutableStateFlow(false)
//    val useDynamicColor : StateFlow<Boolean> = _useDynamicColor
//    init {
//        viewModelScope.launch {
//            userPreferencesRepository.getUseDynamicColor()
//                .collect {
//                    _useDynamicColor.value = it
//                    _setting.value = Settings(darkMode = settings.value.darkMode,
//                        useSystemDarkMode = settings.value.useSystemDarkMode,
//                        theme = settings.value.theme,
//                        dynamicColor = it)
//                }
//        }
//    }

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


    override fun logTag(): String {
        return "SettingsScrnViewModel"
    }

}