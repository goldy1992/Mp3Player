package com.example.mike.mp3player.client.utils;

import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.PlaybackStateWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class TimerUtils {

    public static final int ONE_SECOND = 1000;

    public static int convertToSeconds(long milliseconds) {
        return (int) (milliseconds / ONE_SECOND);
    }

    public static String formatTime(long miliseconds) {
        Date date = new Date(miliseconds);
        SimpleDateFormat timerFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return timerFormat.format(date);
    }

    public static long calculateStartTime(PlaybackStateWrapper playbackStateWrapper) {
        if (playbackStateWrapper != null) {
            PlaybackStateCompat state = playbackStateWrapper.getPlaybackState();
            if (state == null) {
                return 0L;
            }
            Long timestamp = playbackStateWrapper.getTimestanp();

            if (timestamp == null) {
                return 0L;
            }
            long currentTime = System.currentTimeMillis();
            //Log.d("", currentTime)
            long timeDiff = currentTime - timestamp;
            return state.getPosition() + timeDiff;

        }
        return 0L;
    }
}
