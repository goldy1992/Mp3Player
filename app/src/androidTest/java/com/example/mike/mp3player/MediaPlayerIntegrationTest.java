package com.example.mike.mp3player;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.mike.mp3player.client.MediaPlayerActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MediaPlayerIntegrationTest {

    public static final String PACKAGE_NAME = "com.example.mike.mp3player";
    public static final String ANDROID_RESOURCE = "android.resource";
    public static final String TEST_MP3_URL = ANDROID_RESOURCE + "://" + PACKAGE_NAME + "/" + R.raw.test_yomil_dany_jala_jala;

    @Rule
    public final ActivityTestRule activityTestRule = new ActivityTestRule(MediaPlayerActivity.class) {
        @Override
        public Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getTargetContext();
            Uri uri = Uri.parse(TEST_MP3_URL);
            Intent intent = new Intent(targetContext, MediaPlayerActivity.class);
            intent.putExtra("uri", uri);
            return intent;
        }
    };


    @Before
    public void setup() {

    }

    @Test
    public void testPlay()
    {
        MediaPlayerActivity mediaPlayerActivity = (MediaPlayerActivity) activityTestRule.getActivity();
        assertNotNull(mediaPlayerActivity);

        onView(withId(R.id.playPauseButton)).perform(click());
        onView(withId(R.id.playPauseButton)).check(matches(withText("Pause")));
    }

}
