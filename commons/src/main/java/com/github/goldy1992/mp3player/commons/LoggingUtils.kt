package com.github.goldy1992.mp3player.commons

import android.util.Log
import androidx.media3.common.MediaMetadata

object LoggingUtils {


    fun logPlaybackStateCompat(playbackState: Int, tag: String?) {
        val sb = StringBuilder()
        val state = "State: " + Constants.playbackStateDebugMap[playbackState]
        val position = "Position: " + playbackState
        val log = sb.append(state).append("\n").append(position).toString()
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