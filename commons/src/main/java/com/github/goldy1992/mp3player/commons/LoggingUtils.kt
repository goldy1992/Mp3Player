package com.github.goldy1992.mp3player.commons

import android.util.Log
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_AUDIO_ATTRIBUTES_CHANGED
import androidx.media3.common.Player.EVENT_AUDIO_SESSION_ID
import androidx.media3.common.Player.EVENT_AVAILABLE_COMMANDS_CHANGED
import androidx.media3.common.Player.EVENT_CUES
import androidx.media3.common.Player.EVENT_DEVICE_INFO_CHANGED
import androidx.media3.common.Player.EVENT_DEVICE_VOLUME_CHANGED
import androidx.media3.common.Player.EVENT_IS_LOADING_CHANGED
import androidx.media3.common.Player.EVENT_IS_PLAYING_CHANGED
import androidx.media3.common.Player.EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
import androidx.media3.common.Player.EVENT_MEDIA_METADATA_CHANGED
import androidx.media3.common.Player.EVENT_METADATA
import androidx.media3.common.Player.EVENT_PLAYBACK_PARAMETERS_CHANGED
import androidx.media3.common.Player.EVENT_PLAYBACK_STATE_CHANGED
import androidx.media3.common.Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED
import androidx.media3.common.Player.EVENT_PLAYER_ERROR
import androidx.media3.common.Player.EVENT_PLAYLIST_METADATA_CHANGED
import androidx.media3.common.Player.EVENT_PLAY_WHEN_READY_CHANGED
import androidx.media3.common.Player.EVENT_POSITION_DISCONTINUITY
import androidx.media3.common.Player.EVENT_RENDERED_FIRST_FRAME
import androidx.media3.common.Player.EVENT_REPEAT_MODE_CHANGED
import androidx.media3.common.Player.EVENT_SEEK_BACK_INCREMENT_CHANGED
import androidx.media3.common.Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED
import androidx.media3.common.Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED
import androidx.media3.common.Player.EVENT_SKIP_SILENCE_ENABLED_CHANGED
import androidx.media3.common.Player.EVENT_SURFACE_SIZE_CHANGED
import androidx.media3.common.Player.EVENT_TIMELINE_CHANGED
import androidx.media3.common.Player.EVENT_TRACKS_CHANGED
import androidx.media3.common.Player.EVENT_TRACK_SELECTION_PARAMETERS_CHANGED
import androidx.media3.common.Player.EVENT_VIDEO_SIZE_CHANGED
import androidx.media3.common.Player.EVENT_VOLUME_CHANGED
import androidx.media3.common.Player.Events
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_IDLE
import androidx.media3.common.Player.STATE_READY

/**
 * Util object for more visible logging.
 */
object LoggingUtils {

    val playerEventToStringMap = mapOf<@Player.Event Int, String>(
        EVENT_TIMELINE_CHANGED to "EVENT_TIMELINE_CHANGED",
        EVENT_MEDIA_ITEM_TRANSITION to "EVENT_MEDIA_ITEM_TRANSITION",
        EVENT_TRACKS_CHANGED to "EVENT_TRACKS_CHANGED",
        EVENT_IS_LOADING_CHANGED to "EVENT_IS_LOADING_CHANGED",
        EVENT_PLAYBACK_STATE_CHANGED to "EVENT_PLAYBACK_STATE_CHANGED",
        EVENT_PLAY_WHEN_READY_CHANGED to "EVENT_PLAY_WHEN_READY_CHANGED",
        EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED to "EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED",
        EVENT_IS_PLAYING_CHANGED to "EVENT_IS_PLAYING_CHANGED",
        EVENT_REPEAT_MODE_CHANGED to "EVENT_REPEAT_MODE_CHANGED",
        EVENT_SHUFFLE_MODE_ENABLED_CHANGED to "EVENT_SHUFFLE_MODE_ENABLED_CHANGED",
        EVENT_PLAYER_ERROR to "EVENT_PLAYER_ERROR",
        EVENT_POSITION_DISCONTINUITY to "EVENT_POSITION_DISCONTINUITY",
        EVENT_PLAYBACK_PARAMETERS_CHANGED to "EVENT_PLAYBACK_PARAMETERS_CHANGED",
        EVENT_AVAILABLE_COMMANDS_CHANGED to "EVENT_AVAILABLE_COMMANDS_CHANGED",
        EVENT_MEDIA_METADATA_CHANGED to "EVENT_MEDIA_METADATA_CHANGED",
        EVENT_PLAYLIST_METADATA_CHANGED to "EVENT_PLAYLIST_METADATA_CHANGED",
        EVENT_SEEK_BACK_INCREMENT_CHANGED to "EVENT_SEEK_BACK_INCREMENT_CHANGED",
        EVENT_SEEK_FORWARD_INCREMENT_CHANGED to "EVENT_SEEK_FORWARD_INCREMENT_CHANGED",
        EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED to "EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED",
        EVENT_TRACK_SELECTION_PARAMETERS_CHANGED to "EVENT_TRACK_SELECTION_PARAMETERS_CHANGED",
        EVENT_AUDIO_ATTRIBUTES_CHANGED to "EVENT_AUDIO_ATTRIBUTES_CHANGED",
        EVENT_AUDIO_SESSION_ID to "EVENT_AUDIO_SESSION_ID",
        EVENT_VOLUME_CHANGED to "EVENT_VOLUME_CHANGED",
        EVENT_SKIP_SILENCE_ENABLED_CHANGED to "EVENT_SKIP_SILENCE_ENABLED_CHANGED",
        EVENT_SURFACE_SIZE_CHANGED to "EVENT_SURFACE_SIZE_CHANGED",
        EVENT_VIDEO_SIZE_CHANGED to "EVENT_VIDEO_SIZE_CHANGED",
        EVENT_RENDERED_FIRST_FRAME to "EVENT_RENDERED_FIRST_FRAME",
        EVENT_CUES to "EVENT_CUES",
        EVENT_METADATA to "EVENT_METADATA",
        EVENT_DEVICE_INFO_CHANGED to "EVENT_DEVICE_INFO_CHANGED",
        EVENT_DEVICE_VOLUME_CHANGED to "EVENT_DEVICE_VOLUME_CHANGED"
    )

    val playerStateToStringMap = mapOf<@Player.State Int, String>(
        STATE_IDLE to "STATE_IDLE", // 1
        STATE_BUFFERING to "STATE_BUFFERING", // 2
        STATE_READY to "STATE_READY", // 3
        STATE_ENDED to "STATE_ENDED", // 4
    )
    
    val playerRepeatModeToStringMap = mapOf<@Player.RepeatMode Int, String>(
        REPEAT_MODE_OFF to "REPEAT_MODE_OFF", // 0
        REPEAT_MODE_ONE to "REPEAT_MODE_ONE", // 1
        REPEAT_MODE_ALL to "REPEAT_MODE_ALL" // 2
    )

    /**
     * Logs out the [Player.State] by transforming the integer code to an interpretable string.
     */
    fun logPlaybackState(playbackState: Int, tag: String?) {
        val sb = StringBuilder()
        val state = "State: " + (playerStateToStringMap[playbackState] ?: Constants.UNKNOWN)
        val log = sb.append(state).toString()
        Log.i(tag, log)
    }

    /**
     * Logs out the [Player.RepeatMode] by transforming the integer code to an interpretable string.
     */
    fun logRepeatMode(@Player.RepeatMode repeatMode: Int, tag: String) {
        val sb = StringBuilder()
        sb.append("Repeat mode is: ")
        sb.append(playerRepeatModeToStringMap[repeatMode] ?: "Invalid repeat mode")
        Log.i(tag, sb.toString())
    }

    /**
     * Logs out [Player.Events] by transforming the integer code to an interpretable string.
     */
    fun getPlayerEventsLogMessage(playerEvent: Events): String {
        val eventString = mutableListOf<String>()
        for (idx in 0 until playerEvent.size()) {
           eventString.add(playerEventToStringMap[playerEvent[idx]] ?: Constants.UNKNOWN)
        }
        return "events: " + eventString.joinToString()
    }
}