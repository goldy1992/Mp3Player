package com.example.mike.mp3player.client.utils;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.client.PlaybackStateWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class TimerUtils {

    public static final int ONE_SECOND = 1000;
    private static final String LOG_TAG = "TIMER_UTILS";

    public static int convertToSeconds(long milliseconds) {
        return (int) (milliseconds / ONE_SECOND);
    }

    public static String formatTime(long miliseconds) {
        Date date = new Date(miliseconds);
        SimpleDateFormat timerFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        String formattedTime = timerFormat.format(date);
        //Log.d(LOG_TAG, "returning formatted time: " + formattedTime);
        return formattedTime;
    }

    public static long calculateCurrentPlaybackPosition(PlaybackStateWrapper playbackStateWrapper) {
        if (playbackStateWrapper != null) {
            PlaybackStateCompat state = playbackStateWrapper.getPlaybackState();
            if (state == null) {
                return 0L;
            }
            Long timestamp = playbackStateWrapper.getTimestanp();

            if (timestamp == null) {
                return playbackStateWrapper.getPlaybackState().getBufferedPosition();
            }
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - timestamp;
            return state.getPosition() + timeDiff;
        }
        return 0L;
    }
}
