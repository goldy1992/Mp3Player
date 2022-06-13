package com.github.goldy1992.mp3player.service.player.equalizer

import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants.frequencyBands
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import kotlin.math.atan2
import kotlin.math.hypot

val fft = FastFourierTransformer(DftNormalization.STANDARD)

fun createFftSample(original: FloatArray,
                channelCount : Int,
                sampleHz : Int) : AudioSample {

    val doubles  = DoubleArray(original.size)
    original.forEachIndexed { idx, value -> doubles[idx] = value.toDouble()  }
    val ffTransformed : Array<Complex> = fft.transform(doubles, TransformType.FORWARD)

 //   val phase : Array<Double> = getPhase(ffTransformed)
    //     Log.d(logTag(), "phase: ${phase.joinToString(",")}")
    val magnitude : Array<Double> = getMagnitude(ffTransformed)

    return AudioSample(
      //  phase = phase,
      //  magnitude =  magnitude,
        waveformData = original,
        frequencyMap = calculateFrequencyBands(magnitude),
        sampleHz = sampleHz,
        channelCount = channelCount)
}

private fun getMagnitude(fftData: Array<Complex>) : Array<Double> {
    return fftData.map { value -> hypot(value.real, value.imaginary) }.toTypedArray()
}

private fun getPhase(fftData: Array<Complex>) : Array<Double> {
    return fftData.map { value -> atan2(value.imaginary, value.real) }.toTypedArray()
}

private fun calculateFrequencyBands(magnitudes: Array<Double>) : FloatArray {
    val toReturn = FloatArray(frequencyBands.size)
    val numOfFreq = magnitudes.size

    frequencyBands.forEachIndexed { idx, frequencyRange ->

        if (frequencyRange.last <= numOfFreq) {
            var sum = 0.0
            frequencyRange.forEach { range -> sum += magnitudes[range] }
            val average = sum / (frequencyRange.last - frequencyRange.first)
            toReturn[idx] = average.toFloat()
        }

    }

    return toReturn
}