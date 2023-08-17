package com.github.goldy1992.mp3player.client.ui.components.equalizer.circular

import androidx.compose.ui.geometry.Offset

data class CircleCoordinate(
    val offset: Offset,
    val radius : Float,
    /** Radians */
    val angle : Float
)
data class CubicBezierCurve(
    val from : CircleCoordinate,
    val to : CircleCoordinate,
    val controlPoint1 : CircleCoordinate,
    val controlPoint2: CircleCoordinate
)
