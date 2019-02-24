package com.example.mike.mp3player.commons;

import android.media.MediaPlayer;

public final class LegacyUtilMethods {

    public static int getCurrentPlaybackPosition(MediaPlayer currentMediaPlayer, boolean useBufferedPosition, int bufferedPosition) {
        int playbackPosition = currentMediaPlayer.getCurrentPosition();
        if (useBufferedPosition) {
            useBufferedPosition = false;
            playbackPosition = bufferedPosition;
        }

        /**
         * This is to resolve a bug on some phones where the playback position is 0 (using <= 0 to
         * resolve any negative position errors) :- the MediaPlayer JNI throws an IllegalStateException.
         * To resolve this problem 1 is returned instead of zero (assuming in 99.99% the duration is >= 1)
         */
        if (playbackPosition <= 0) { // if at the beginning at track, or for some reason negative
            if (currentMediaPlayer.getDuration() >= 1) {
                return 1;
            }
        }
        return playbackPosition;
    }
}
