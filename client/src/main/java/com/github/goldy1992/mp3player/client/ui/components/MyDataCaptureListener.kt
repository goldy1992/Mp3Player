package com.github.goldy1992.mp3player.client.ui.components

import android.media.audiofx.Visualizer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.commons.LogTagger
import org.apache.commons.lang3.ArrayUtils.isNotEmpty
import kotlin.math.abs


class MyDataCaptureListener(private val array: MutableLiveData<String>) : Visualizer.OnDataCaptureListener, LogTagger {


    override fun onWaveFormDataCapture(
        visualizer: Visualizer?,
        waveform: ByteArray?,
        samplingRate: Int
    ) {
//        var bytes : String = "["
//        if (isNotEmpty(waveform)) {
//            for (n in waveform!!) {
//                bytes += "$n,"
//            }
//            bytes += "]"
//        }
//        array.postValue(bytes)
        if (visualizer?.enabled == true) {
            val waveformInt = visualizer.getWaveForm(waveform)
            Log.d(logTag(), "wave: ${waveformInt}")
            array.postValue(waveformInt.toString())
        }
    }

    override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {
        var bytes : String = "["
        if (isNotEmpty(fft)) {
            for (n in fft!!) {
                bytes += "$n,"
            }
            bytes += "]"
        }
        array.postValue(bytes)

        if (visualizer?.enabled == true && fft != null) {
            val n: Int = fft.size
            val magnitudes = FloatArray(n / 2 + 1)
            val phases = FloatArray(n / 2 + 1)
            magnitudes[0] = abs(fft[0].toInt()).toFloat() // DC

            magnitudes[n / 2] = abs(fft[1].toInt()).toFloat() // Nyquist

            phases[0] = 0.also { phases[n / 2] = it.toFloat() }.toFloat()
            for (k in 1 until n / 2) {
                val i = k * 2
                magnitudes[k] = Math.hypot(fft!![i].toDouble(), fft!![i + 1].toDouble()).toFloat()
                phases[k] = Math.atan2(fft!![i + 1].toDouble(), fft!![i].toDouble()).toFloat()
            }
     //       val fftInt = visualizer.getFft(fft)
            Log.d(logTag(), "fft: ${bytes}")
        }
    }

    override fun logTag(): String {
        return "MyDataCaptureListener"
    }
}