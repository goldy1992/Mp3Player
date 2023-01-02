package com.github.goldy1992.mp3player.client

import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandFive
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandTwentyFour
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Test class for [AudioDataProcessor].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AudioDataProcessorTest : CoroutineTestBase() {

    private val audioDataProcessor : AudioDataProcessor = AudioDataProcessor(super.dispatcher)

    /** Setup */
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    /**
     * GIVEN: An empty [AudioSample]
     * WHEN: The [AudioSample] is processed.
     * THEN: AN empty [AudioSample] is returned.
     * */
    @Test
    fun testProcessEmptySample()= testScope.runTest {
        val result : FloatArray = audioDataProcessor.processAudioData(AudioSample.NONE, FrequencyBandTwentyFour())
        assertEquals(0, result.size)
    }

    /**
     * GIVEN: We want to process the data over [FrequencyBandFive]
     * AND: There are less than 4000 frequency values (4000, being the max frequency)
     * WHEN: The [AudioSample] is processed.
     * THEN: 5 audio bands are returned.
     */
    @Test
    fun testProcessLessThanRequiredValues() = testScope.runTest {
        val size = 64
        val testArray = FloatArray(size)
        for (i in 0 until size) {
            testArray[i] = 1.0f
        }
        val testSample = AudioSample(waveformData = testArray)

        val result = audioDataProcessor.processAudioData(testSample, FrequencyBandFive())
        assertEquals(5, result.size)
    }
}