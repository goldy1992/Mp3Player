package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import android.content.Context
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType

object VisualizerUtils {

    fun getVisualizerName(type: VisualizerType, context : Context) : String {
        return when (type) {
            VisualizerType.BAR -> context.getString(R.string.bars)
            VisualizerType.FOUNTAIN -> context.getString(R.string.fountain)
            VisualizerType.LINE ->  context.getString(R.string.smooth_line)
            VisualizerType.CIRCULAR -> context.getString(R.string.circular)
            VisualizerType.PIE_CHART -> context.getString(R.string.pie_chart)
        }
    }
}