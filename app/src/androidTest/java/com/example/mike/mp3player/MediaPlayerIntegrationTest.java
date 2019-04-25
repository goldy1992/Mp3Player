package com.example.mike.mp3player;

import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.commons.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mediaPlayerActivityIntent = new Intent(targetContext, MediaPlayerActivity.class);
        MediaSession mediaSession = new MediaSession(targetContext, "MEDIA_SESSION");
        MediaSessionCompat mediaSessionCompat = MediaSessionCompat.fromMediaSession(targetContext, mediaSession);
        mediaPlayerActivityIntent.putExtra(Constants.MEDIA_SESSION, mediaSessionCompat.getSessionToken());
        ActivityScenario.launch(mediaPlayerActivityIntent);
    }

    @Test
    public void testStartUp() {
        Log.i("", "info");
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
