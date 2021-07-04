package com.github.goldy1992.mp3player.client.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.google.accompanist.pager.ExperimentalPagerApi


class MainActivityUnitTestImpl : MainActivity() {

    @ExperimentalPagerApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @Composable
    override fun Ui(
        mediaRepository : MediaRepository,
        mediaBrowserAdapter : MediaBrowserAdapter,
        mediaControllerAdapter : MediaControllerAdapter,
        userPreferencesRepository : UserPreferencesRepository
    ) {
        Column() {
            // Do nothing
        }
    }
}