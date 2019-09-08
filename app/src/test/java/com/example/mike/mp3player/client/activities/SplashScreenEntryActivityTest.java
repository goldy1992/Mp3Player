package com.example.mike.mp3player.client.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
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

    private ActivityScenario<SplashScreenEntryActivity> scenario;

    @Before
    public void setup() {
        this.scenario = ActivityScenario.launch(SplashScreenEntryActivity.class);
    }

    @Test
    public void testNonRootTask() {
        scenario.onActivity((SplashScreenEntryActivity splashActivity) -> {
            SplashScreenEntryActivity spiedActivity = spy(splashActivity);
            Context context = InstrumentationRegistry.getInstrumentation().getContext();
            Intent intent = new Intent(context, SplashScreenEntryActivity.class);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setAction(Intent.ACTION_MAIN);
            when(spiedActivity.getIntent()).thenReturn(intent);
            when(spiedActivity.isTaskRoot()).thenReturn(false);
            spiedActivity.onCreate(null);
            assertTrue(splashActivity.isFinishing());
        });


    }

    @Test
    public void testOnRequestPermissionResult() {
        assertTrue(true);
    }

    @Test
    public void testOnConnected() {
        scenario.onActivity((SplashScreenEntryActivity splashActivity) -> {
            SplashScreenEntryActivity spiedActivity = spy(splashActivity);
            spiedActivity.setPermissionGranted(true);
            spiedActivity.setSplashScreenFinishedDisplaying(true);
            spiedActivity.onConnected();
            verify(spiedActivity, times(1)).startActivityForResult(any(Intent.class), eq(APP_TERMINATED));
        });
    }

    @Test
    public void testOnPermissionGranted() {
        scenario.onActivity((SplashScreenEntryActivity splashActivity) -> {
            SplashScreenEntryActivity spiedActivity = spy(splashActivity);
            doNothing().when(spiedActivity).runOnUiThread(any(Runnable.class));
            spiedActivity.onPermissionGranted();
            assertTrue(spiedActivity.isPermissionGranted());
        });
    }

    @Test
    public void testOnActivityResultValidAppTerminated() {
        scenario.onActivity((SplashScreenEntryActivity splashActivity) -> {
            SplashScreenEntryActivity spiedActivity = spy(splashActivity);
            spiedActivity.onActivityResult(APP_TERMINATED, APP_TERMINATED, null);
            verify(spiedActivity, times(1)).finish();
        });
    }

    @Test
    public void testOnActivityResultInvalidAppTerminated() {
        scenario.onActivity((SplashScreenEntryActivity splashActivity) -> {
            final int NOT_APP_TERMINATED = -1;
            SplashScreenEntryActivity spiedActivity = spy(splashActivity);
            spiedActivity.onActivityResult(NOT_APP_TERMINATED, NOT_APP_TERMINATED, null);
            verify(spiedActivity, never()).finish();
        });
    }

    @Test
    public void testOnRequestPermissionsResultAccepted() {
        scenario.onActivity((SplashScreenEntryActivity splashActivity) -> {
            splashActivity = spy(SplashScreenEntryActivity.class);
            doNothing().when(splashActivity).onPermissionGranted();
            final int requestCode = 200;
            String[] permissions = new String[]{WRITE_EXTERNAL_STORAGE};
            int[] grantResults = new int[]{PackageManager.PERMISSION_GRANTED};
            splashActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);
            verify(splashActivity, times(1)).onPermissionGranted();
        });
    }

    @Test
    public void testOnRequestPermissionsResultRejected() {
        scenario.onActivity((SplashScreenEntryActivity splashActivity) -> {
            final int requestCode = 200;
            String[] permissions = new String[]{WRITE_EXTERNAL_STORAGE};
            int[] grantResults = new int[]{PackageManager.PERMISSION_DENIED};
            splashActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);
            assertTrue(splashActivity.isFinishing());
        });
    }

    @Test
    public void testOnRequestPermissionsResultEmpty() {
        scenario.onActivity((SplashScreenEntryActivity splashActivity) -> {
            final int requestCode = 200;
            String[] permissions = new String[]{};
            int[] grantResults = new int[]{};
            splashActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);
            assertTrue(splashActivity.isFinishing());
        });
    }

//    @After
//    public void tearDown() {
//        if (null != splashScreenEntryActivity) {
//            Thread thread = splashScreenEntryActivity.getThread();
//            if (null != thread) {
//                thread.stop();
//            }
//        }
//    }

}