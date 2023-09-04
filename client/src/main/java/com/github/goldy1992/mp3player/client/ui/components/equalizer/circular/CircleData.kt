package com.github.goldy1992.mp3player.client.ui.components.equalizer.circular

import androidx.compose.ui.geometry.Offset

/**
 * Represents a Cubic Bezier curve relative to a compose canvas, i.e. in terms of an [Offset]
 */
data class CubicBezierCurveOffset(
    /**
     * The point that the curve will finish at.
     */
    val to : Offset,
    /**
     * The first bezier control point.
     */
    val controlPoint1 : Offset,
    /**
     * The second bezier control point.
     */
    val controlPoint2: Offset
)
