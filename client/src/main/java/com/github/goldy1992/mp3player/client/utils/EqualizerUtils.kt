package com.github.goldy1992.mp3player.client.utils

import androidx.compose.ui.unit.Dp

fun calculateBarWidth(containerWidth : Dp, numOfBars : Int, spaceBetweenBars: Dp) : Dp {

    val numerator : Float = (containerWidth + (spaceBetweenBars * ( 1 - numOfBars))).value
    val barWidthFloat : Float = numerator / numOfBars

    val isValidConstraints = numerator > 0f
    if (isValidConstraints) {
        return Dp(barWidthFloat)
    }
    return Dp(barWidthFloat)
}