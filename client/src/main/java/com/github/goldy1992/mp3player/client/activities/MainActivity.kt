package com.github.goldy1992.mp3player.client.activities

import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.client.ui.rememberWindowSizeClass
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * The Main Activity
 */
@AndroidEntryPoint(MainActivityBase::class)
open class MainActivity : Hilt_MainActivity() {

    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }


    @kotlin.OptIn(ExperimentalPagerApi::class, InternalCoroutinesApi::class,
        ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class
    )
    override fun ui(startScreen : Screen) {
        setContent{
            var windowSizeClass = rememberWindowSizeClass()
            ComposeApp(
                mediaBrowserAdapter = this.mediaBrowserAdapter,
                mediaControllerAdapter = this.mediaControllerAdapter,
                userPreferencesRepository = this.userPreferencesRepository,
                windowSize = windowSizeClass,
                startScreen = startScreen)
        }
    }
}