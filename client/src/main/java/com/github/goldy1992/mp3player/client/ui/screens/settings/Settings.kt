package com.github.goldy1992.mp3player.client.ui.screens.settings

import com.github.goldy1992.mp3player.client.ui.Theme

data class Settings


    constructor(
        val darkMode : Boolean = false,
        val useSystemDarkMode : Boolean = true,
        val theme : Theme = Theme.BLUE,
        val dynamicColor : Boolean = false
    ) {

    enum class Type {
        DARK_MODE,
        USE_SYSTEM_DARK_MODE,
        THEME,
        DYNAMIC_COLOR
    }

}