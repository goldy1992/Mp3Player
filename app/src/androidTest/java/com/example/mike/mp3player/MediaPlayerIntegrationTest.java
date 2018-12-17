package com.example.mike.mp3player;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.mike.mp3player.client.MediaPlayerActivity;
import com.example.mike.mp3player.client.view.SeekerBar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.Rule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

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

@RunWith(Runner.class)
public class MediaPlayerIntegrationTest extends org.junit.runner.Runner {

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


    @BeforeEach
    public void setup() {

    }

    @org.junit.jupiter.api.Test
    public void testPlay() throws InterruptedException {
        MediaPlayerActivity mediaPlayerActivity = (MediaPlayerActivity) activityTestRule.getActivity();
        assertNotNull(mediaPlayerActivity);
        assertTrue(mediaPlayerActivity.getCounter().getView().getText().equals("00:00"));
        onView(withId(R.id.playPauseButton)).perform(click());
        Thread.sleep(2000);
        assertTrue(mediaPlayerActivity.getCounter().getCurrentTime() >= 0);

        onView(withId(R.id.playPauseButton)).check(matches(withText("Pause")));

        onView(withId(R.id.seekBar)).perform(TestUtils.setProgress(50));
        SeekerBar seekerBar = (SeekerBar) mediaPlayerActivity.findViewById(R.id.seekBar);

        assertEquals(mediaPlayerActivity.getCounter().getView().getText().equals("00:00"), false);
    }

    @Override
    public Description getDescription() {
        return null;
    }

    @Override
    public void run(RunNotifier notifier) {

    }
}
