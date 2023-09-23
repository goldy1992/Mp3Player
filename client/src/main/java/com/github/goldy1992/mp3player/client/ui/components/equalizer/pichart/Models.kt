package com.github.goldy1992.mp3player.client.ui.components.equalizer.pichart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

data class PieSegment (
    val color : Color,
    val arcOffset : Offset,
    val size : Size
)

data class ColorRange (
    val red : Float,
    val green : Float,
    val blue : Float
)