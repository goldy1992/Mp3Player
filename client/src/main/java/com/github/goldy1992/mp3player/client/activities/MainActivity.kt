package com.github.goldy1992.mp3player.client.activities

import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.github.goldy1992.mp3player.client.ui.ComposeApp
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


    @ExperimentalPagerApi
    @InternalCoroutinesApi
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun ui() {
        setContent{
            ComposeApp(
                mediaRepository =  this.mediaRepository,
                mediaBrowserAdapter = this.mediaBrowserAdapter,
                mediaControllerAdapter = this.mediaControllerAdapter,
                userPreferencesRepository = this.userPreferencesRepository,
                showSplashScreen = this.showSplashScreen)
        }
    }
}