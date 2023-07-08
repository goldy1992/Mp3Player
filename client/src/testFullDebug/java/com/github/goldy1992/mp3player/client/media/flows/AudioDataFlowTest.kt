package com.github.goldy1992.mp3player.client.media.flows

import android.os.Bundle
import androidx.media3.session.SessionCommand
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants.AUDIO_DATA
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class AudioDataFlowTest : MediaFlowTestBase<AudioSample>() {

    @Test
    fun testAudioDataFlowIsCollected() {
        val resultState = initTestFlow(AudioSample.NONE)
        val customCommandFlow = MutableStateFlow(SessionCommandEventHolder.DEFAULT)

        AudioDataFlow.create(testScope, customCommandFlow) { v -> resultState.value = v }
        val testData = AudioSample(waveformData = listOf(1f, 2f, 3f).toFloatArray())
        customCommandFlow.value = createSessionCommand(testData)

        testScope.advanceUntilIdle()
        assertEquals(testData.waveformData.contentHashCode(), resultState.value.waveformData.contentHashCode())
        testScope.cancel()
    }

    @Test
    fun testNonAudioDataIsFiltered() {
        val resultState = initTestFlow(AudioSample.NONE)
        val customCommandFlow = MutableStateFlow(SessionCommandEventHolder.DEFAULT)


        AudioDataFlow.create(testScope, customCommandFlow) { v -> resultState.value = v }
        val testData = AudioSample(waveformData = listOf(1f, 2f, 3f).toFloatArray())
        val sessionCommand = createSessionCommand(testData, "NOT_AUDIO_DATA")
        customCommandFlow.value = sessionCommand

        testScope.advanceUntilIdle()
        assertNotEquals(testData.waveformData.contentHashCode(), resultState.value.waveformData.contentHashCode())
        testScope.cancel()
    }

    private fun createSessionCommand(audioSample: AudioSample, customAction : String = AUDIO_DATA) : SessionCommandEventHolder {
        val bundle = Bundle()
        bundle.putSerializable(customAction, audioSample)
        val sessionCommand = SessionCommand(customAction, bundle)
        return SessionCommandEventHolder(sessionCommand, Bundle())
    }
}