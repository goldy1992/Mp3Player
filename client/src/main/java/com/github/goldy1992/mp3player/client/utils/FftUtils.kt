package com.github.goldy1992.mp3player.client.utils

import android.util.Log
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBand
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandFive
import com.github.goldy1992.mp3player.commons.AudioSample
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import kotlin.math.atan2
import kotlin.math.hypot

private const val logTag = "FFTUtils"

val fft = FastFourierTransformer(DftNormalization.STANDARD)

fun createFftTransformedAudioSample(audioSample: AudioSample) : AudioSample {
    val original = audioSample.waveformData
    val channelCount : Int = audioSample.channelCount
    val sampleHz : Int = audioSample.sampleHz
    val doubles  = DoubleArray(original.size)
    original.forEachIndexed { idx, value -> doubles[idx] = value.toDouble()  }
    val ffTransformed : Array<Complex> = fft.transform(doubles, TransformType.FORWARD)
    val magnitude : Array<Double> = getMagnitude(ffTransformed)

    return AudioSample(
        magnitude =  magnitude,
        waveformData = original,
        sampleHz = sampleHz,
        channelCount = channelCount)
}

fun getMagnitude(fftData: Array<Complex>) : Array<Double> {
    return fftData.map { value -> hypot(value.real, value.imaginary) }.toTypedArray()
}

fun getPhase(fftData: Array<Complex>) : Array<Double> {
    return fftData.map { value -> atan2(value.imaginary, value.real) }.toTypedArray()
}

fun calculateFrequencyBandsAverages(values: Array<Double>, frequencyBand: FrequencyBand = FrequencyBandFive()): FloatArray {
    val frequencyBands = frequencyBand.frequencyBands()
    val toReturn = FloatArray(frequencyBands.size)
    val numOfFreq = values.size

    frequencyBands.forEachIndexed { idx, frequencyRange ->

        if (frequencyRange.last < numOfFreq) {
            var sum = 0.0
            frequencyRange.forEach { range -> sum += values[range] }
            val average = sum / (frequencyRange.last - frequencyRange.first)
            toReturn[idx] = average.toFloat()
        }
    }

    return toReturn
}

