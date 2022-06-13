package com.github.goldy1992.mp3player.commons

import java.io.Serializable

data class AudioSample
    constructor(
        val phase : Array<Double> = emptyArray(),
        val magnitude : Array<Double> = emptyArray(),
        val waveformData : FloatArray = FloatArray(0),
        val frequencyMap : FloatArray = FloatArray(0),
        val sampleHz : Int = 128000,
        val channelCount : Int = 2
    ) : Serializable {

    companion object {
       val NONE = AudioSample(emptyArray(), emptyArray())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioSample

        if (!phase.contentEquals(other.phase)) return false
        if (!magnitude.contentEquals(other.magnitude)) return false
        if (!frequencyMap.contentEquals(other.frequencyMap)) return false
        if (sampleHz != other.sampleHz) return false
        if (channelCount != other.channelCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = phase.contentHashCode()
        result = 31 * result + magnitude.contentHashCode()
        result = 31 * result + frequencyMap.contentHashCode()
        result = 31 * result + sampleHz
        result = 31 * result + channelCount
        return result
    }
}
