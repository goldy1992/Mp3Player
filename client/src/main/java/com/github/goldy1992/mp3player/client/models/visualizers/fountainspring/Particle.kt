package com.github.goldy1992.mp3player.client.models.visualizers.fountainspring


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.github.goldy1992.mp3player.client.models.visualizers.fountainspring.Constants.g
import com.github.goldy1992.mp3player.client.utils.TimeUtils.convertNanoSecondsToRuntimeSpeed
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private const val logTag = "Particle"

data class Particle
    constructor(
        val startPosition : Offset,
        val removePoint : Float = startPosition.y * 0.9f,
        val hMax : Float,
        val isFalling : Boolean,
        val initialVelocity : Float = 7.5f,
        val angle : Double = 90.0,
        val radius : Float = 50f,
        var width : Float = 10f,
        val color : Color = Color.Blue,
        val x : Float = 0f,
        val y : Float = 0f,
        val elapsedTimeDeciSeconds : Float = 0f,
    ) {
    
    companion object {
        fun createParticle(startOffset: Offset,
                           initialVelocity : Float,
                           hMax: Float,
                           color : Color) : Particle {
            val startY = startOffset.y
            val startX = startOffset.x
            val radius = Random.nextDouble(2.0, 5.0)
            val angle = Random.nextDouble(85.0, 95.0)
            return Particle(
                x = startX,
                y = startY,
                hMax = hMax,
                isFalling = false,
                startPosition = Offset(startX, startY),
                initialVelocity = initialVelocity,
                radius = radius.toFloat(),
                color = color,
                angle = angle)
        }

    }
    

    override fun toString(): String {
        return "x: $x, y: $y, elapsedTimeSecs: $elapsedTimeDeciSeconds, startPos: $startPosition, initialVelocity: $initialVelocity, angle $angle"
    }

    fun update(timeDiffNanoSecs : Long) : Particle {
        val elapsedTimeSecs = this.elapsedTimeDeciSeconds + convertNanoSecondsToRuntimeSpeed(timeDiffNanoSecs)
        val x = this.startPosition.x + (this.initialVelocity * cos(Math.toRadians(this.angle)).toFloat() * this.elapsedTimeDeciSeconds)
        val y = this.startPosition.y + ((this.initialVelocity * sin(Math.toRadians(this.angle)).toFloat() * this.elapsedTimeDeciSeconds) - ((g * this.elapsedTimeDeciSeconds * this.elapsedTimeDeciSeconds) / 2f))

        val newAlpha = if (elapsedTimeSecs > 18f) 0f else (1-(elapsedTimeSecs / 18f))
        return Particle(
            startPosition = this.startPosition,
            initialVelocity = this.initialVelocity,
            isFalling = y > this.y,
            x = x,
            y = y,
            elapsedTimeDeciSeconds = elapsedTimeSecs,
            color = Color(
                red=this.color.red,
                green=this.color.green,
                blue= this.color.blue,
                alpha=newAlpha),
            angle = this.angle,
            hMax = this.hMax,
            radius = this.radius)
    }


    fun shouldRemove() : Boolean {
        return isFalling && (this.y > this.removePoint)
    }
}

