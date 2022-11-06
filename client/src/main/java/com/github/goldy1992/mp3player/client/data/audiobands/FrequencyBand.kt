package com.github.goldy1992.mp3player.client.data.audiobands

abstract class FrequencyBand {

    abstract fun frequencyBands() : List<IntRange>
}