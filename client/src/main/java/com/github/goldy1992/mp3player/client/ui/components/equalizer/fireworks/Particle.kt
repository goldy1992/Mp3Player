package com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

private const val logTag = "Particle"

data class Particle
constructor(
    val startPosition : Offset,
    val timeToReachHmaxMs : Float,
    val initialVelocity : Float = 7.5f,
    val angle : Double = 90.0,
    val radius : Float = 50f,
    var width : Float = 10f,
    val color : Color = Color.Blue,

    val x : Float = 0f,
    val y : Float = 0f,
    val elapsedTimeSecs : Float = 0f,
    val currentFrameTimeNs :  Long) {

    val isFalling : Boolean = elapsedTimeSecs > timeToReachHmaxMs

    override fun toString(): String {
        return "x: ${x}, y: ${y}, elapsedTimeSecs: ${elapsedTimeSecs}, startPos: ${startPosition}, initialVelocity: ${initialVelocity}, timeToReachHmax: ${timeToReachHmaxMs}, angle ${angle}, currentFrameTimeNs: $currentFrameTimeNs"
    }
}

