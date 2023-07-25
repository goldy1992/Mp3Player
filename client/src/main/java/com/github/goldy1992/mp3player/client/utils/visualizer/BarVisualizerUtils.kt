package com.github.goldy1992.mp3player.client.utils.visualizer

import android.util.Log

private const val logTag = "EqualizerUtils"

fun calculateBarWidthPixels(containerWidth: Float, numOfBars: Int, spaceBetweenBars: Float) : Float {

    val numerator : Float = containerWidth - (spaceBetweenBars * ( 1 + numOfBars))
    val barWidthFloat : Float = numerator / numOfBars

    val isValidConstraints = numerator > 0f
    if (isValidConstraints) {
        return barWidthFloat
    }
    return barWidthFloat
}

fun calculateBarSpacingPixels(containerWidthPx: Float, numOfBars: Int, barWidthPx: Float) : Float {
    if (numOfBars <= -1) {
        Log.w(
            logTag, "calculateBarSpacingPixels: Invalid number of bars $numOfBars when " +
                "calculating spacing, returning 0f")
        return 0f
    }
    val numerator : Float = containerWidthPx - (barWidthPx * numOfBars)
    val denominator = numOfBars + 1
    val barSpacing : Float = numerator / denominator

    val isValidConstraints = barSpacing > 0f

    if (!isValidConstraints) {
        Log.w(logTag, "calculateBarSpacingPixels: Negative bar spacing calculated: $barSpacing")
    }
    return barSpacing
}