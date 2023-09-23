package com.github.goldy1992.mp3player.client.utils.visualizer

import androidx.compose.ui.geometry.Offset
import com.github.goldy1992.mp3player.client.models.visualizers.fountainspring.Constants
import com.github.goldy1992.mp3player.client.ui.components.equalizer.fountainspring.FountainSpringVisualizer
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Utils object for the [FountainSpringVisualizer].
 */
object FountainSpringUtils {

    /**
     *  Calculates the initial velocity, u, required to reach the max height
     *  i.e hMax = ((u^2)*(sin(x)^2)) / 2g (see google docs)
     *  => u = sqrt( abs((hMax * 2g) / (sin^2(x)))  )
     *  abs() is used to ensure NaN is not returned.
     *
     *  where hMax :- the maximum particle height
     *        x    :- the angle of projection, in radians
     *        g    :- the constant acceleration of gravity 9.8 m/s^2
     *        u    :- the initial velocity at the time of projection
     *
     * @param hMax The maximum height the particle should reach i.e. the velocity on the y axis is 0
     * @param angle THe angle of projection about the x-axis in radians.
     * @return u, the initial velocity at the time of projection.
     */
    fun calculateInitialVelocityGivenHMax(hMax : Float, angle : Double) : Float {
        val denominator = sin(angle).pow(2)
        val numerator = hMax * 2 * Constants.g // g is negative therefore we should
        val u =  sqrt(abs(numerator / denominator)).toFloat()
        return u * -1
    }

    fun generateSpringSpawnPoints(canvasWidth : Float,
                                  canvasHeight : Float,
                                  insetPx: Float,
                                  particleWidth: Float,
                                  numberOfFrequencies : Int)
    : List<Offset> {
        val spawnHeight : Float = canvasHeight //+ 10f
        val lengthOfDivision : Float = canvasWidth - (insetPx * 2) - (particleWidth * numberOfFrequencies)
        val widthBetweenSpawns : Float = lengthOfDivision / (numberOfFrequencies + 1)//if (numberOfFrequencies <= 1) lengthOfDivision else lengthOfDivision / (numberOfFrequencies - 1)

        val toReturn = arrayListOf<Offset>()
        for (i in 1 .. numberOfFrequencies) {
            val offsetValue = Offset(x = insetPx + (widthBetweenSpawns * i) + (particleWidth * i),
                y = spawnHeight)
            toReturn.add(offsetValue)
        }
        return toReturn.toList()
    }
}