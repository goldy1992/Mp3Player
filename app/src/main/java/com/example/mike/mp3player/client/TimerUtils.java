package com.example.mike.mp3player.client;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimerUtils {

    public static final int ONE_THOUSAND = 1000;

    public static int convertToSeconds(long milliseconds) {
        return (int) (milliseconds / ONE_THOUSAND);
    }

    public static String formatTime(long miliseconds) {
        Date date = new Date(miliseconds);
        SimpleDateFormat timerFormat = new SimpleDateFormat("mm:ss");
        return timerFormat.format(date);
    }
}
