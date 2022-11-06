package com.github.goldy1992.mp3player.client.utils

fun calculateBarWidthPixels(containerWidth: Float, numOfBars: Int, spaceBetweenBars: Float) : Float {

    val numerator : Float = containerWidth - (spaceBetweenBars * ( 1 + numOfBars))
    val barWidthFloat : Float = numerator / numOfBars

    val isValidConstraints = numerator > 0f
    if (isValidConstraints) {
        return barWidthFloat
    }
    return barWidthFloat
}