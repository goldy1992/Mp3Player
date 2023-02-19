package com.github.goldy1992.mp3player.commons

import android.util.Log

object LoggingUtils {


    fun logPlaybackState(playbackState: Int, tag: String?) {
        val sb = StringBuilder()
        val state = "State: " + (Constants.playbackStateDebugMap[playbackState]?: "UNKNOWN")
        val log = sb.append(state).toString()
        Log.i(tag, log)
    }

    fun logRepeatMode(repeatMode: Int, tag: String?) {
        val sb = StringBuilder()
        sb.append("Repeat mode is: ")
        sb.append(Constants.repeatModeDebugMap[repeatMode] ?: "Invalid repeat mode")
        Log.i(tag, sb.toString())
    }

    fun logShuffleMode(shuffleMode: Boolean, tag: String?) {
        val sb = StringBuilder()
        sb.append("Shuffle mode is: $shuffleMode")
        Log.i(tag, sb.toString())
    }
}