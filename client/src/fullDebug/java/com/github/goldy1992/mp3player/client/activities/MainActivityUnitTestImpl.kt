package com.github.goldy1992.mp3player.client.activities

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import com.github.goldy1992.mp3player.commons.Screen

class MainActivityUnitTestImpl : MainActivity() {

    override fun ui(startScreen: Screen) {
        setContent {
            Column {
                // Do nothing
            }
        }
    }
}