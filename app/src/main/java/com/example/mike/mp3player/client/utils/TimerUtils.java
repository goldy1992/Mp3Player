package com.example.mike.mp3player.client.utils;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.mike.mp3player.commons.Constants.TIMESTAMP;

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

    public static long calculateStartTime(PlaybackStateCompat state) {
        if (state == null || state.getExtras() == null)
        {
            return 0L;
        }
        Long timestamp = state.getExtras().getLong(TIMESTAMP);

        if (timestamp == null) {
            return 0L;
        }
        long currentTime= System.currentTimeMillis();
        //Log.d("", currentTime)
        long timeDiff =  - timestamp;
        return state.getPosition() + timeDiff;

    }
}
