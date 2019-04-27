package com.example.mike.mp3player;

import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.commons.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MediaPlayerIntegrationTest {
//
//    public static final String PACKAGE_NAME = "com.example.mike.mp3player";
//    public static final String ANDROID_RESOURCE = "android.resource";
//    public static final String TEST_MP3_URL = null; // ANDROID_RESOURCE + "://" + PACKAGE_NAME + "/" + R.raw.test_yomil_dany_jala_jala;
    @Rule
    public ActivityScenarioRule<MediaPlayerActivity> mediaPlayerActivityScenario;
    Intent mediaPlayerActivityIntent;

    @Before
    public void setUp() {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        mediaPlayerActivityIntent = new Intent(targetContext, MediaPlayerActivity.class);
//        MediaSession mediaSession = new MediaSession(targetContext, "MEDIA_SESSION");
//        MediaSessionCompat mediaSessionCompat = MediaSessionCompat.fromMediaSession(targetContext, mediaSession);
//        mediaPlayerActivityIntent.putExtra(Constants.MEDIA_SESSION, mediaSessionCompat.getSessionToken());
//        ActivityScenario.launch(mediaPlayerActivityIntent);
    }

    @Test
    public void testStartUp() {
        Log.i("", "info");
        String s = null;
        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("ls");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }


    }
//    @Rule
//    public final ActivityTestRule activityTestRule = new ActivityTestRule(MediaPlayerActivity.class) {
//        @Override
//        public Intent getActivityIntent() {
//            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//            Uri uri = Uri.parse(TEST_MP3_URL);
//            Intent intent = new Intent(targetContext, MediaPlayerActivity.class);
//            intent.putExtra("uri", uri);
//            return intent;
//
//        }
//    };
//
//    @Before
//    public void setup() {    }
//
//    @Test
//    public void testPlay() throws InterruptedException {
//        MediaPlayerActivity mediaPlayerActivity = (MediaPlayerActivity) activityTestRule.getActivity();
//        assertNotNull(mediaPlayerActivity);
//        TimeCounter counter = mediaPlayerActivity.getPlaybackTrackerFragment().getCounter();
//        CharSequence counterText = counter.getView().getText();
//        assertTrue(counterText.equals("00:00"));
//        onView(withId(R.id.playPauseButton)).perform(click());
//        Thread.sleep(2000);
//        assertTrue(counter.getCurrentPosition() >= 0);
//
//        onView(withId(R.id.playPauseButton)).check(matches(withText("Pause")));
//
//        onView(withId(R.id.seekBar)).perform(TestUtils.setProgress(50));
//        SeekerBar seekerBar = (SeekerBar) mediaPlayerActivity.findViewById(R.id.seekBar);
//
//        assertEquals(counterText.equals("00:00"), false);
//    }

}
