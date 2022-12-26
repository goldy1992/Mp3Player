package com.github.goldy1992.mp3player.client.ui.screens.settings

import com.github.goldy1992.mp3player.client.ui.Theme

data class Settings
    constructor(
        val darkMode : Boolean = false,
        val useSystemDarkMode : Boolean = true,
        val theme : Theme = Theme.BLUE
    ) {
}