package com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Particle
constructor(
    val startPosition : Offset,
    val initialVelocity : Float = 7.5f,
    val angle : Double = 90.0,
    val radius : Float = 50f,
    val color : Color = Color.Blue,
    val x : Float = 0f,
    val y : Float = 0f,
    val t : Float = 0f,
    val currentFrameTime :  Long = 0L ) {

    override fun toString(): String {
        return "x: ${x}, y: ${y}, t: ${t}, startPos: ${startPosition}, initialVelocity: ${initialVelocity}, angle ${angle},"
    }
}

