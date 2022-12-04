package com.github.goldy1992.mp3player.client.ui.components.equalizer.fountainspring


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

private const val logTag = "Particle"

data class Particle
constructor(
    val startPosition : Offset,
    //val timeToReachHmaxSecs : Float,
    val hMax : Float,
    val isFalling : Boolean,
    val initialVelocity : Float = 7.5f,
    val angle : Double = 90.0,
    val radius : Float = 50f,
    var width : Float = 10f,
    val color : Color = Color.Blue,

    val x : Float = 0f,
    val y : Float = 0f,
    val elapsedTimeDeciseconds : Float = 0f,
    val currentFrameTimeNs :  Long) {

    override fun toString(): String {
        return "x: ${x}, y: ${y}, elapsedTimeSecs: ${elapsedTimeDeciseconds}, startPos: ${startPosition}, initialVelocity: ${initialVelocity}, angle ${angle}, currentFrameTimeNs: $currentFrameTimeNs"
    }
}

