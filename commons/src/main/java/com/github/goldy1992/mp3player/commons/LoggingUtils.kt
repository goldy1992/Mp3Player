package com.github.goldy1992.mp3player.commons

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log

object LoggingUtils {


    fun logPlaybackStateCompat(stateCompat: PlaybackStateCompat, tag: String?) {
        val sb = StringBuilder()
        val state = "State: " + Constants.playbackStateDebugMap[stateCompat.state]
        val position = "Position: " + stateCompat.position
        val log = sb.append(state).append("\n").append(position).toString()
        Log.i(tag, log)
    }

    fun logMetaData(metadataCompat: MediaMetadataCompat?, tag: String?) {
        val sb = StringBuilder()
        if (metadataCompat != null && metadataCompat.description != null) {
            val description = metadataCompat.description
            val title = "title: " + description.title.toString()
            val duration = "duration: " + metadataCompat.bundle.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
            val log = sb.append(title).append("\n").append(duration).toString()
            Log.i(tag, log)
        } else {
            Log.i(tag, sb.append("null metadat or description").toString())
        }
    }

    fun logRepeatMode(repeatMode: Int, tag: String?) {
        val sb = StringBuilder()
        sb.append("Repeat mode is: ")
        when (repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ALL -> sb.append("REPEAT_MODE_ALL")
            PlaybackStateCompat.REPEAT_MODE_NONE -> sb.append("REPEAT_MODE_NONE")
            PlaybackStateCompat.REPEAT_MODE_ONE -> sb.append("REPEAT_MODE_ONE")
            else -> sb.append("invalid repeat mode")
        }
        Log.i(tag, sb.toString())
    }

    fun logShuffleMode(shuffleMode: Int, tag: String?) {
        val sb = StringBuilder()
        sb.append("Shuffle mode is: ")
        when (shuffleMode) {
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> sb.append("SHUFFLE_MODE_ALL")
            PlaybackStateCompat.SHUFFLE_MODE_NONE -> sb.append("SHUFFLE_MODE_NONE")
            PlaybackStateCompat.SHUFFLE_MODE_INVALID -> sb.append("SHUFFLE_MODE_INVALID")
            else -> sb.append("SHUFFLE_MODE_GROUP")
        }
        Log.i(tag, sb.toString())
    }
}