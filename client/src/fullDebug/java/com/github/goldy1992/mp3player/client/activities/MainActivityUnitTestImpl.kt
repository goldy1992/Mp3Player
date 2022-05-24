package com.github.goldy1992.mp3player.client.activities

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi


class MainActivityUnitTestImpl : MainActivity() {



    override fun ui(startScreen: Screen) {
        setContent {
            Column() {
                // Do nothing
            }
        }
    }
}