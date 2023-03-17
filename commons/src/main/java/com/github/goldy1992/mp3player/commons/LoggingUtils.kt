package com.github.goldy1992.mp3player.commons

import android.util.Log
import androidx.media3.common.Player
import androidx.media3.common.Player.*

object LoggingUtils {
    fun logPlaybackState(playbackState: Int, tag: String?) {
        val sb = StringBuilder()
        val state = "State: " + (Constants.playbackStateDebugMap[playbackState] ?: "UNKNOWN")
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

    fun getPlayerEventsLogMessage(playerEvent: Player.Events): String {
        var toReturn = "event:"
        for (idx in 0 until playerEvent.size()) {
            when (val event: @Event Int = playerEvent.get(idx)) {
                EVENT_TIMELINE_CHANGED -> toReturn += " EVENT_TIMELINE_CHANGED,"
                EVENT_MEDIA_ITEM_TRANSITION -> toReturn += " EVENT_MEDIA_ITEM_TRANSITION,"
                EVENT_TRACKS_CHANGED -> toReturn += " EVENT_TRACKS_CHANGED,"
                EVENT_IS_LOADING_CHANGED -> toReturn += " EVENT_IS_LOADING_CHANGED,"
                EVENT_PLAYBACK_STATE_CHANGED -> toReturn += " EVENT_PLAYBACK_STATE_CHANGED,"
                EVENT_PLAY_WHEN_READY_CHANGED -> toReturn += " EVENT_PLAY_WHEN_READY_CHANGED,"
                EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED -> toReturn += " EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED,"
                EVENT_IS_PLAYING_CHANGED -> toReturn += " EVENT_IS_PLAYING_CHANGED,"
                EVENT_REPEAT_MODE_CHANGED -> toReturn += " EVENT_REPEAT_MODE_CHANGED,"
                EVENT_SHUFFLE_MODE_ENABLED_CHANGED -> toReturn += " EVENT_SHUFFLE_MODE_ENABLED_CHANGED,"
                EVENT_PLAYER_ERROR -> toReturn += " EVENT_PLAYER_ERROR,"
                EVENT_POSITION_DISCONTINUITY -> toReturn += " EVENT_POSITION_DISCONTINUITY,"
                EVENT_PLAYBACK_PARAMETERS_CHANGED -> toReturn += " EVENT_PLAYBACK_PARAMETERS_CHANGED,"
                EVENT_AVAILABLE_COMMANDS_CHANGED -> toReturn += " EVENT_AVAILABLE_COMMANDS_CHANGED,"
                EVENT_MEDIA_METADATA_CHANGED -> toReturn += " EVENT_MEDIA_METADATA_CHANGED,"
                EVENT_PLAYLIST_METADATA_CHANGED -> toReturn += " EVENT_PLAYLIST_METADATA_CHANGED,"
                EVENT_SEEK_BACK_INCREMENT_CHANGED -> toReturn += " EVENT_SEEK_BACK_INCREMENT_CHANGED,"
                EVENT_SEEK_FORWARD_INCREMENT_CHANGED -> toReturn += " EVENT_SEEK_FORWARD_INCREMENT_CHANGED"
                else -> toReturn += " unknown_event: $event, "
            }
        }
        return toReturn
    }

}