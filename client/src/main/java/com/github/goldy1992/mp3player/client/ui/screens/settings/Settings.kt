package com.github.goldy1992.mp3player.client.ui.screens.settings


data class Settings


    constructor(
        val darkMode : Boolean = false,
        val useSystemDarkMode : Boolean = true,
        val dynamicColor : Boolean = false,
        val language : String = "en"
    ) {

    enum class Type {
        DARK_MODE,
        USE_SYSTEM_DARK_MODE,
        DYNAMIC_COLOR,
        LANGUAGE
    }

    companion object {
        val DEFAULT = Settings()
    }

}