package com.github.goldy1992.mp3player.client.data.audiobands

/**
 *
 */
class FrequencyBandFive : FrequencyBand() {
    companion object {
        private val SUB_BASS = IntRange(20, 60)
        private val BASS = IntRange(61, 250)
        private val LOW_MIDRANGE = IntRange(251, 500)
        private val MIDRANGE = IntRange(501, 2000)
        private val UPPER_MIDRANGE = IntRange(2001, 4000)
        private val frequencyBands = listOf(SUB_BASS, BASS, LOW_MIDRANGE, MIDRANGE, UPPER_MIDRANGE)
    }
    override fun frequencyBands(): List<IntRange> {
        return frequencyBands
    }
}