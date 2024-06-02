package com.github.goldy1992.mp3player.commons

import android.util.Log
import java.io.Serializable

/**
 * Represents an Audio Sample that will be sent from the Audio Processor within the Media Playback
 * Service.
 */
data class AudioSample
    constructor(
        val phase : Array<Double> = emptyArray(),
        val magnitude : Array<Double> = emptyArray(),
        val waveformData : FloatArray = FloatArray(0),
        val sampleHz : Int = 128000,
        val channelCount : Int = 2
    ) : Serializable, LogTagger {

    companion object {
       val NONE = AudioSample(emptyArray(), emptyArray())
    }

    override fun logTag(): String {
        return "AudioSample"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
         //   Log.v(logTag(), "equals() Samples found to be equal and point to the same object")
            return true
        }
        if (javaClass != other?.javaClass) {
       //     Log.v(logTag(), "equals() java classes NOT equal, return false, this: $this, other@ $other")
            return false
        }

        other as AudioSample

        if (!phase.contentEquals(other.phase)) {
//Log.v(logTag(), "equals() phase NOT equal, return false, this: $this, other@ $other")
            return false
        }
        if (!magnitude.contentEquals(other.magnitude)) {
       //     Log.v(logTag(), "equals() magnitude NOT equal, return false, this: $this, other@ $other")
            return false
        }
        if (!waveformData.contentEquals(other.waveformData)) {
      //      Log.v(logTag(), "equals() waveformData NOT equal, return false, this: $this, other@ $other")
            return false
        }
        if (sampleHz != other.sampleHz) {
      //      Log.v(logTag(), "equals() ampleHz NOT equal, return false, this: $this, other@ $other")
            return false
        }
        if (channelCount != other.channelCount) {
    //        Log.v(logTag(), "equals() channelCount NOT equal, return false, this: $this, other@ $other")
            return false
        }

      //  Log.v(logTag(), "equals() AudioSamples this: ${this.hashCode()} and other: ${other.hashCode()} are found to be equal")
        return true
    }

    override fun hashCode(): Int {
        var result = phase.contentHashCode()
        result = 31 * result + magnitude.contentHashCode()
        result = 31 * result + waveformData.contentHashCode()
        result = 31 * result + sampleHz
        result = 31 * result + channelCount
        Log.v(logTag(), "hashCode() hashCode: ${result}")
        return result
    }

    override fun toString(): String {
        return super.toString()
    }
}
