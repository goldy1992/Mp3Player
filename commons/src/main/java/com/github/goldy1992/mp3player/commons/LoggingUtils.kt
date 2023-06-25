package com.github.goldy1992.mp3player.commons

import android.util.Log
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
import androidx.media3.common.Player.Event
import androidx.media3.common.Player.Events

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

    fun getPlayerEventsLogMessage(playerEvent: Events): String {
        var toReturn = "event:"
        for (idx in 0 until playerEvent.size()) {
            when (val event: @Event Int = playerEvent[idx]) {
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
                EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED -> toReturn += "EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED"
                EVENT_TRACK_SELECTION_PARAMETERS_CHANGED -> toReturn += "EVENT_TRACK_SELECTION_PARAMETERS_CHANGED"
                EVENT_AUDIO_ATTRIBUTES_CHANGED -> toReturn += "EVENT_AUDIO_ATTRIBUTES_CHANGED"
                EVENT_AUDIO_SESSION_ID -> toReturn += "EVENT_AUDIO_SESSION_ID"
                EVENT_VOLUME_CHANGED -> toReturn += "EVENT_VOLUME_CHANGED"
                EVENT_SKIP_SILENCE_ENABLED_CHANGED -> toReturn += "EVENT_SKIP_SILENCE_ENABLED_CHANGED"
                EVENT_SURFACE_SIZE_CHANGED -> toReturn += "EVENT_SURFACE_SIZE_CHANGED"
                EVENT_VIDEO_SIZE_CHANGED -> toReturn += "EVENT_VIDEO_SIZE_CHANGED"
                EVENT_RENDERED_FIRST_FRAME -> toReturn += "EVENT_RENDERED_FIRST_FRAME"
                EVENT_CUES -> toReturn += "EVENT_CUES"
                EVENT_METADATA -> toReturn += "EVENT_METADATA"
                EVENT_DEVICE_INFO_CHANGED -> toReturn += "EVENT_DEVICE_INFO_CHANGED"
                EVENT_DEVICE_VOLUME_CHANGED -> toReturn += "EVENT_DEVICE_VOLUME_CHANGED"
                else -> toReturn += " unknown_event: $event, "
            }
        }
        return toReturn
    }
    /*

    int EVENT_TIMELINE_CHANGED = 0;
    /** {@link #getCurrentMediaItem()} changed or the player started repeating the current item. */
    int EVENT_MEDIA_ITEM_TRANSITION = 1;
    /** {@link #getCurrentTracks()} changed. */
    int EVENT_TRACKS_CHANGED = 2;
    /** {@link #isLoading()} ()} changed. */
    int EVENT_IS_LOADING_CHANGED = 3;
    /** {@link #getPlaybackState()} changed. */
    int EVENT_PLAYBACK_STATE_CHANGED = 4;
    /** {@link #getPlayWhenReady()} changed. */
    int EVENT_PLAY_WHEN_READY_CHANGED = 5;
    /** {@link #getPlaybackSuppressionReason()} changed. */
    int EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED = 6;
    /** {@link #isPlaying()} changed. */
    int EVENT_IS_PLAYING_CHANGED = 7;
    /** {@link #getRepeatMode()} changed. */
    int EVENT_REPEAT_MODE_CHANGED = 8;
    /** {@link #getShuffleModeEnabled()} changed. */
    int EVENT_SHUFFLE_MODE_ENABLED_CHANGED = 9;
    /** {@link #getPlayerError()} changed. */
    int EVENT_PLAYER_ERROR = 10;
    /**
     * A position discontinuity occurred. See {@link Listener#onPositionDiscontinuity(PositionInfo,
     * PositionInfo, int)}.
     */
    int EVENT_POSITION_DISCONTINUITY = 11;
    /** {@link #getPlaybackParameters()} changed. */
    int EVENT_PLAYBACK_PARAMETERS_CHANGED = 12;
    /** {@link #isCommandAvailable(int)} changed for at least one {@link Command}. */
    int EVENT_AVAILABLE_COMMANDS_CHANGED = 13;
    /** {@link #getMediaMetadata()} changed. */
    int EVENT_MEDIA_METADATA_CHANGED = 14;
    /** {@link #getPlaylistMetadata()} changed. */
    int EVENT_PLAYLIST_METADATA_CHANGED = 15;
    /** {@link #getSeekBackIncrement()} changed. */
    int EVENT_SEEK_BACK_INCREMENT_CHANGED = 16;
    /** {@link #getSeekForwardIncrement()} changed. */
    int EVENT_SEEK_FORWARD_INCREMENT_CHANGED = 17;
    /** {@link #getMaxSeekToPreviousPosition()} changed. */
    int EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED = 18;
    /** {@link #getTrackSelectionParameters()} changed. */
    int EVENT_TRACK_SELECTION_PARAMETERS_CHANGED = 19;
    /** {@link #getAudioAttributes()} changed. */
    int EVENT_AUDIO_ATTRIBUTES_CHANGED = 20;
    /** The audio session id was set. */
    int EVENT_AUDIO_SESSION_ID = 21;
    /** {@link #getVolume()} changed. */
    int EVENT_VOLUME_CHANGED = 22;
    /** Skipping silences in the audio stream is enabled or disabled. */
    int EVENT_SKIP_SILENCE_ENABLED_CHANGED = 23;
    /** The size of the surface onto which the video is being rendered changed. */
    int EVENT_SURFACE_SIZE_CHANGED = 24;
    /** {@link #getVideoSize()} changed. */
    int EVENT_VIDEO_SIZE_CHANGED = 25;
    /**
     * A frame is rendered for the first time since setting the surface, or since the renderer was
     * reset, or since the stream being rendered was changed.
     */
    int EVENT_RENDERED_FIRST_FRAME = 26;
    /** {@link #getCurrentCues()} changed. */
    int EVENT_CUES = 27;
    /** Metadata associated with the current playback time changed. */
    int EVENT_METADATA = 28;
    /** {@link #getDeviceInfo()} changed. */
    int EVENT_DEVICE_INFO_CHANGED = 29;
    /** {@link #getDeviceVolume()} changed. */
    int EVENT_DEVICE_VOLUME_CHANGED = 30;
    
    */

}