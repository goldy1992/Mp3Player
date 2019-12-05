package com.github.goldy1992.mp3player.commons;

import android.media.PlaybackParams;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION;

public final class LoggingUtils {

    public static void logPlaybackParams(PlaybackParams playbackParams, final String LOG_TAG) {
        StringBuilder sb = new StringBuilder();
        String pitch = "pitch: " + playbackParams.getPitch()+ "\n";
        String audioFallbackMode = "audiofallbackmode: ";
        switch (playbackParams.getAudioFallbackMode()) {
            case PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT: audioFallbackMode +=  "AUDIO_FALLBACK_MODE_DEFAULT"; break;
            case PlaybackParams.AUDIO_FALLBACK_MODE_FAIL: audioFallbackMode +=  "AUDIO_FALLBACK_MODE_FAIL"; break;
            case PlaybackParams.AUDIO_FALLBACK_MODE_MUTE: audioFallbackMode +=  "AUDIO_FALLBACK_MODE_MUTE"; break;
            default: audioFallbackMode = "none";
        }
        audioFallbackMode += "\n";
        String speed = "speed: " + playbackParams.getSpeed() + "\n";


        String log = sb.append(pitch).append(audioFallbackMode).append(speed).toString();
        Log.i(LOG_TAG, log);
    }

    public static void logPlaybackStateCompat(PlaybackStateCompat stateCompat, final String LOG_TAG) {
        StringBuilder sb = new StringBuilder();
        String state = "State: " + Constants.playbackStateDebugMap.get(stateCompat.getState());
        String position = "Position: " + stateCompat.getPosition();
        String log = sb.append(state).append("\n").append(position).toString();
        Log.i(LOG_TAG, log);
    }

    public static void logMetaData(MediaMetadataCompat metadataCompat, final String LOG_TAG) {
        StringBuilder sb = new StringBuilder();
        if (metadataCompat != null && metadataCompat.getDescription() != null) {
            MediaDescriptionCompat description = metadataCompat.getDescription();
            String title = "title: " + description.getTitle().toString();
            String duration ="duration: " + metadataCompat.getBundle().getLong(METADATA_KEY_DURATION);
            String log = sb.append(title).append("\n").append(duration).toString();
            Log.i(LOG_TAG, log);
        } else {
            Log.i(LOG_TAG, sb.append("null metadat or description").toString());
        }

    }

    public static void logRepeatMode(int RepeatMode, final String LOG_TAG) {
        StringBuilder sb = new StringBuilder();
        sb.append("Repeat mode is: ");
        switch (RepeatMode) {
            case PlaybackStateCompat.REPEAT_MODE_ALL: sb.append("REPEAT_MODE_ALL");
                break;
            case PlaybackStateCompat.REPEAT_MODE_NONE: sb.append("REPEAT_MODE_NONE");
                break;
            case PlaybackStateCompat.REPEAT_MODE_ONE: sb.append("REPEAT_MODE_ONE");
                break;
            default:
                sb.append("invalid repeat mode");
        }
        Log.i(LOG_TAG, sb.toString());
    }

    public static void logShuffleMode(int shuffleMode, final String LOG_TAG) {
        StringBuilder sb = new StringBuilder();
        sb.append("Shuffle mode is: ");
        switch (shuffleMode) {
            case PlaybackStateCompat.SHUFFLE_MODE_ALL: sb.append("SHUFFLE_MODE_ALL"); break;
            case PlaybackStateCompat.SHUFFLE_MODE_NONE: sb.append("SHUFFLE_MODE_NONE"); break;
            case PlaybackStateCompat.SHUFFLE_MODE_INVALID: sb.append("SHUFFLE_MODE_INVALID"); break;
            default: sb.append("SHUFFLE_MODE_GROUP");
        }
    }
}
