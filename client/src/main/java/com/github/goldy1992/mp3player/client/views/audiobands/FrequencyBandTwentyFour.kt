package com.github.goldy1992.mp3player.client.views.audiobands

class FrequencyBandTwentyFour : FrequencyBand() {


    private val FREQUENCY_BAND_LIMITS = listOf(
        IntRange(0, 20),
        IntRange(21, 25),
        IntRange(26, 32),
        IntRange(33, 40),
        IntRange(41, 50),
        IntRange(51, 63),
        IntRange(64, 80),
        IntRange(81, 100),
        IntRange(101, 125),
        IntRange(126, 160),
        IntRange(161, 200),
        IntRange(201, 250),
        IntRange(251, 315),
        IntRange(316, 400),
        IntRange(401, 500),
        IntRange(501, 630),
        IntRange(631, 800),
        IntRange(801, 1000),
        IntRange(1001, 1250),
        IntRange(1251, 1600),
        IntRange(1601, 2000),
        IntRange(2001, 2500),
        IntRange(2501, 3150),
        IntRange(3151, 4095)
    )

    override fun frequencyBands(): List<IntRange> {
        return FREQUENCY_BAND_LIMITS
    }
}