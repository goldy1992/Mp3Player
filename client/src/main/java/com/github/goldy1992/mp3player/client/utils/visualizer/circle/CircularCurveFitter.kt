package com.github.goldy1992.mp3player.client.utils.visualizer.circle

import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CubicBezierCurveOffset
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlin.math.cos
import kotlin.math.sin

interface CircularCurveFitter : LogTagger {

    val center: Offset
    val minRadius : Float
    val insetPx : Float
    val maxFreqRadius: Double
    val maxFreq : Double

    fun offsetCoordinates(frequencies: List<Float>) : List<Offset> {

        val toReturn = mutableListOf<Offset>()

        var angle = 0.0
        if (frequencies.isNotEmpty()) {
            val spacing = ((2 * Math.PI) / frequencies.count()).toFloat()

            for (frequency in frequencies) {
                val radius = minRadius + (maxFreqRadius * (frequency / maxFreq))
                val x = (radius * cos(angle)).toFloat() + center.x
                val y = (radius * sin(angle)).toFloat() + center.y

                toReturn.add(Offset(x, y))
                angle += spacing
            }
           // toReturn.add(toReturn[0])
        } else {
            Log.v(logTag(), "Empty frequency list, using default")
            val spacing = ((2 * Math.PI) / 15).toFloat()
            for (i in 1..15) {
                val x = (minRadius * cos(angle)).toFloat() + center.x
                val y = (minRadius * sin(angle)).toFloat() + center.y
                toReturn.add(Offset(x, y))
                angle += spacing
            }
           // toReturn.add(toReturn[0])

        }
        return toReturn
    }

    fun generateBeziers(frequencies : List<Float>) : List<CubicBezierCurveOffset>


    fun getType() : String
}