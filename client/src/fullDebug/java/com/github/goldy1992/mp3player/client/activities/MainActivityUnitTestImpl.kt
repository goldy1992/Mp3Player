package com.github.goldy1992.mp3player.client.activities

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.media3.common.util.UnstableApi
import com.github.goldy1992.mp3player.commons.Screen


@UnstableApi
class MainActivityUnitTestImpl : MainActivity() {



    override fun ui() {
        setContent {
            Column() {
                // Do nothing
            }
        }
    }
}