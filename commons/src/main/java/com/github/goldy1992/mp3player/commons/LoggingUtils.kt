package com.github.goldy1992.mp3player.commons

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log

object LoggingUtils {
    fun logPlaybackStateCompat(stateCompat: PlaybackStateCompat, LOG_TAG: String?) {
        val sb = StringBuilder()
        val state = "State: " + Constants.playbackStateDebugMap[stateCompat.state]
        val position = "Position: " + stateCompat.position
        val log = sb.append(state).append("\n").append(position).toString()
        Log.i(LOG_TAG, log)
    }

    fun logMetaData(metadataCompat: MediaMetadataCompat?, LOG_TAG: String?) {
        val sb = StringBuilder()
        if (metadataCompat != null && metadataCompat.description != null) {
            val description = metadataCompat.description
            val title = "title: " + description.title.toString()
            val duration = "duration: " + metadataCompat.bundle.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
            val log = sb.append(title).append("\n").append(duration).toString()
            Log.i(LOG_TAG, log)
        } else {
            Log.i(LOG_TAG, sb.append("null metadat or description").toString())
        }
    }

    fun logRepeatMode(RepeatMode: Int, LOG_TAG: String?) {
        val sb = StringBuilder()
        sb.append("Repeat mode is: ")
        when (RepeatMode) {
            PlaybackStateCompat.REPEAT_MODE_ALL -> sb.append("REPEAT_MODE_ALL")
            PlaybackStateCompat.REPEAT_MODE_NONE -> sb.append("REPEAT_MODE_NONE")
            PlaybackStateCompat.REPEAT_MODE_ONE -> sb.append("REPEAT_MODE_ONE")
            else -> sb.append("invalid repeat mode")
        }
        Log.i(LOG_TAG, sb.toString())
    }

    fun logShuffleMode(shuffleMode: Int, LOG_TAG: String?) {
        val sb = StringBuilder()
        sb.append("Shuffle mode is: ")
        when (shuffleMode) {
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> sb.append("SHUFFLE_MODE_ALL")
            PlaybackStateCompat.SHUFFLE_MODE_NONE -> sb.append("SHUFFLE_MODE_NONE")
            PlaybackStateCompat.SHUFFLE_MODE_INVALID -> sb.append("SHUFFLE_MODE_INVALID")
            else -> sb.append("SHUFFLE_MODE_GROUP")
        }
    }
}