package com.github.goldy1992.mp3player.client.ui

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.percentOffset
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.swipeWithVelocity
import kotlin.math.absoluteValue
import kotlin.math.hypot
import kotlin.math.roundToLong


const val LongSwipeDistance = 0.95f
const val MediumSwipeDistance = 0.8f
const val ShortSwipeDistance = 0.45f

const val FastVelocity = 4000f
const val MediumVelocity = 1500f
const val SlowVelocity = 300f

fun SemanticsNodeInteraction.swipeAcrossCenterWithVelocity(
    velocity: Float,
    distancePercentageX: Float = 0f,
    distancePercentageY: Float = 0f,
): SemanticsNodeInteraction = performGesture {
    val startOffset = percentOffset(
        x = 0.5f - distancePercentageX / 2,
        y = 0.5f - distancePercentageY / 2
    )
    val endOffset = percentOffset(
        x = 0.5f + distancePercentageX / 2,
        y = 0.5f + distancePercentageY / 2
    )
    try {
        swipeWithVelocity(
            start = startOffset,
            end = endOffset,
            endVelocity = velocity,
        )
    } catch (e: IllegalArgumentException) {
        // swipeWithVelocity throws an exception if the given distance + velocity isn't feasible:
        // https://issuetracker.google.com/182477143. To work around this, we catch the exception
        // and instead run a swipe() with a computed duration instead. This is not perfect,
        // but good enough.
        val distance = hypot(endOffset.x - startOffset.x, endOffset.y - startOffset.y)
        swipe(
            start = startOffset,
            end = endOffset,
            durationMillis = ((distance.absoluteValue / velocity) * 1000).roundToLong(),
        )
    }
}


fun SemanticsNodeInteraction.swipeAcrossCenter(
    distancePercentage: Float,
    velocity: Float,
    swipeLeft : Boolean
): SemanticsNodeInteraction = swipeAcrossCenterWithVelocity(
    distancePercentageX = if (swipeLeft) -distancePercentage else distancePercentage,
    velocity = velocity,
)