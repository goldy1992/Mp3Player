package com.github.goldy1992.mp3player.client.activities

import androidx.activity.compose.setContent
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.client.ui.rememberWindowSizeClass
import com.github.goldy1992.mp3player.commons.Screen
import dagger.hilt.android.AndroidEntryPoint

/**
 * The Main Activity
 */
@AndroidEntryPoint(MainActivityBase::class)
open class MainActivity : Hilt_MainActivity() {

    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }


    override fun ui(startScreen : Screen) {
        setContent{
            var windowSizeClass = rememberWindowSizeClass()
            ComposeApp(
                userPreferencesRepository = this.userPreferencesRepository,
                windowSize = windowSizeClass,
                startScreen = startScreen)
        }
    }
}