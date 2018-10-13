package com.example.mike.mp3player.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimerUtilsTest {

    @Test
    public void testConvertToSecondsOneSecond() {
        long milliseconds = 1000;
        int result = TimerUtils.convertToSeconds(milliseconds);
        int expect = 1;
        assertEquals(expect, result);
    }

    @Test
    public void testConvertToSecondsMilisecondsToRound() {
        long milliseconds = 17972;
        int result = TimerUtils.convertToSeconds(milliseconds);
        int expect = 17;
        assertEquals(expect, result);
    }

    @Test
    public void testFormatTimeOneSecond()
    {
        long milliseconds = 1000;
        String result = TimerUtils.formatTime(milliseconds);
        String expect = "00:01";
        assertEquals(expect, result);

    }
    @Test
    public void testFormatTimeRoundableTime()
    {
        long milliseconds = 78756;
        String result = TimerUtils.formatTime(milliseconds);
        String expect = "01:18";
        assertEquals(expect, result);

    }


}