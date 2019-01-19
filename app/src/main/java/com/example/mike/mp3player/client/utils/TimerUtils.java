package com.example.mike.mp3player.client.utils;

import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static long calculateCurrentPlaybackPosition(PlaybackStateCompat state) {
            if (state == null) {
                return 0L;
            }
            if (state.getState() != PlaybackStateCompat.STATE_PLAYING) {
                return state.getPosition();
            } else {
                Long timestamp = state.getLastPositionUpdateTime();

                if (timestamp == null) {
                    return state.getBufferedPosition();
                }
                long currentTime = SystemClock.elapsedRealtime();
                long timeDiff = currentTime - timestamp;
                return state.getPosition() + timeDiff;
            }
    }
}
