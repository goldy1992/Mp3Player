package com.example.mike.mp3player.client.activities;

import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.client.activities.SplashScreenEntryActivity.APP_TERMINATED;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SplashScreenEntryActivityTest {

    private SplashScreenEntryActivity splashScreenEntryActivity;

    @Before
    public void setup() {
        this.splashScreenEntryActivity = Robolectric.buildActivity(SplashScreenEntryActivity.class).create().get();
    }

    @Test
    public void testOnRequestPermissionResult() {
        assertTrue(true);
    }

    @Test
    public void testOnConnected() {
        SplashScreenEntryActivity spiedActivity = spy(splashScreenEntryActivity);
        spiedActivity.setPermissionGranted(true);
        spiedActivity.setSplashScreenFinishedDisplaying(true);
        spiedActivity.onConnected();
        verify(spiedActivity, times(1)).startActivityForResult(any(Intent.class), eq(APP_TERMINATED));
    }

    @Test
    public void testOnPermissionGranted() {
        SplashScreenEntryActivity spiedActivity = spy(splashScreenEntryActivity);
        doNothing().when(spiedActivity).runOnUiThread(any(Runnable.class));
        spiedActivity.onPermissionGranted();
        assertTrue(spiedActivity.isPermissionGranted());
    }

    @Test
    public void testOnActivityResultValidAppTerminated() {
        SplashScreenEntryActivity spiedActivity = spy(splashScreenEntryActivity);
        spiedActivity.onActivityResult(APP_TERMINATED, APP_TERMINATED , null);
        verify(spiedActivity, times(1)).finish();
    }

    @Test
    public void testOnActivityResultInvalidAppTerminated() {
        final int NOT_APP_TERMINATED = -1;
        SplashScreenEntryActivity spiedActivity = spy(splashScreenEntryActivity);
        spiedActivity.onActivityResult(NOT_APP_TERMINATED, NOT_APP_TERMINATED, null);
        verify(spiedActivity, never()).finish();
    }

    @After
    public void tearDown() {
        if (null != splashScreenEntryActivity) {
            Thread thread = splashScreenEntryActivity.getThread();
            if (null != thread) {
                thread.stop();
            }
        }
    }

}