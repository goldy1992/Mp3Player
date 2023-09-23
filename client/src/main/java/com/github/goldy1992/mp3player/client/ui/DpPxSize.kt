package com.github.goldy1992.mp3player.client.ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DpPxSize(
    val widthPx : Float,
    val heightPx : Float,
    val widthDp : Dp,
    val heightDp : Dp
) {
    companion object {

        val DEFAULT = DpPxSize(0f, 0f, 0.dp, 0.dp)
        fun createDpPxSizeFromPx(widthPx : Float, heightPx : Float, density: Density) : DpPxSize {
            val widthDp = (widthPx / density.density).dp
            val heightDp = (heightPx / density.density).dp
            return DpPxSize(widthPx = widthPx,
                            heightPx = heightPx,
                            widthDp = widthDp,
                            heightDp = heightDp)
        }

        fun createDpPxSizeFromDp(widthDp : Dp, heightDp : Dp, density: Density) : DpPxSize {
            val widthPx : Float = widthDp.value * density.density
            val heightPx = heightDp.value * density.density
            return DpPxSize(widthPx = widthPx,
                heightPx = heightPx,
                widthDp = widthDp,
                heightDp = heightDp)
        }
    }

    override fun toString(): String {
        return "widthPx: $widthPx, heightPx: $heightPx, widthDp: $widthDp, heightDp: $heightDp"
    }
}
