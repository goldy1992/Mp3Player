package com.github.goldy1992.mp3player.client.data.audiobands

/**
 *
 */
class FrequencyBandFive : FrequencyBand() {

    val SUB_BASS = IntRange(20, 60)
    val BASS = IntRange(61, 250)
    val LOW_MIDRANGE = IntRange(251, 500)
    val MIDRANGE = IntRange(501, 2000)
    val UPPER_MIDRANGE = IntRange(2001, 4000)
    val frequencyBands = listOf(SUB_BASS, BASS, LOW_MIDRANGE, MIDRANGE, UPPER_MIDRANGE)

    override fun frequencyBands(): List<IntRange> {
        return frequencyBands
    }
}