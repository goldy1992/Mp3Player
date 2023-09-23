package com.github.goldy1992.mp3player.client.models.visualizers.fountainspring

import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset

/**
 * @constructor
 * @param index The index in the frequency phase list that this Spring corresponds to.
 * @param offset The [Offset] from the [Canvas] origin.
 * @param particles The [List] of [Particle]s that have originated at this spring.
 */
data class Spring(
    val index : Int,
    val offset : Offset,
    val particles : List<Particle>
)