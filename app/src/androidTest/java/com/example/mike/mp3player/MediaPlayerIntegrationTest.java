package com.example.mike.mp3player;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.views.SeekerBar;
import com.example.mike.mp3player.client.views.TimeCounter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MediaPlayerIntegrationTest {

    public static final String PACKAGE_NAME = "com.example.mike.mp3player";
    public static final String ANDROID_RESOURCE = "android.resource";
    public static final String TEST_MP3_URL = ANDROID_RESOURCE + "://" + PACKAGE_NAME + "/" + R.raw.test_yomil_dany_jala_jala;

    @Rule
    public final ActivityTestRule activityTestRule = new ActivityTestRule(MediaPlayerActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Uri uri = Uri.parse(TEST_MP3_URL);
            Intent intent = new Intent(targetContext, MediaPlayerActivity.class);
            intent.putExtra("uri", uri);
            return intent;
        }
    };

    @Before
    public void setup() {    }

    @Test
    public void testPlay() throws InterruptedException {
        MediaPlayerActivity mediaPlayerActivity = (MediaPlayerActivity) activityTestRule.getActivity();
        assertNotNull(mediaPlayerActivity);
        TimeCounter counter = mediaPlayerActivity.getPlaybackTrackerFragment().getCounter();
        CharSequence counterText = counter.getView().getText();
        assertTrue(counterText.equals("00:00"));
        onView(withId(R.id.playPauseButton)).perform(click());
        Thread.sleep(2000);
        assertTrue(counter.getCurrentPosition() >= 0);

        onView(withId(R.id.playPauseButton)).check(matches(withText("Pause")));

        onView(withId(R.id.seekBar)).perform(TestUtils.setProgress(50));
        SeekerBar seekerBar = (SeekerBar) mediaPlayerActivity.findViewById(R.id.seekBar);

        assertEquals(counterText.equals("00:00"), false);
    }

}
