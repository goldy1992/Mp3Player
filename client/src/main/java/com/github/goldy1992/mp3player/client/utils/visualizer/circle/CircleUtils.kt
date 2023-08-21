package com.github.goldy1992.mp3player.client.utils.visualizer.circle

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

object CircleUtils {

    fun calculateCenter(size: IntSize) : Offset {
        return Offset(size.width / 2f, size.height / 2f)
    }
}