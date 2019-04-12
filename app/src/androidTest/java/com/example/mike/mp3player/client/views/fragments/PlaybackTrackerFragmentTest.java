package com.example.mike.mp3player.client.views.fragments;

import android.content.Intent;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;
@MediumTest
@RunWith(AndroidJUnit4.class)
public class PlaybackTrackerFragmentTest {

    FragmentScenario<PlaybackTrackerFragment> fragmentScenario;
    FragmentScenario.FragmentAction<PlaybackTrackerFragment> playbackTrackerFragmentFragmentAction;
    @Before
    public void init(){
       fragmentScenario =
               FragmentScenario.launchInContainer(PlaybackTrackerFragment.class);
       fragmentScenario.onFragment(fragment -> initFragment(fragment));
        playbackTrackerFragmentFragmentAction = new FragmentScenario.FragmentAction<PlaybackTrackerFragment>() {
            @Override
            public void perform(@NonNull PlaybackTrackerFragment fragment) {
                MediaMetadataCompat m = new MediaMetadataCompat.Builder().putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 20000).build();
                fragment.onMetadataChanged(m);
            }
        };
    }

    @Test
    public void firstTest() {
        fragmentScenario.moveToState(Lifecycle.State.CREATED);
        fragmentScenario.onFragment(playbackTrackerFragmentFragmentAction);
        //InstrumentationRegistry.getInstrumentation().get
//        onView(allOf(withId(R.id.playback_tracker),
//                allOf(withChild( allOf(withId(R.id.timerLayout))), withChild(withId(R.id.duration))))
       ViewInteraction v = onView(withId(R.id.view)).check(matches(withText(containsString("20"))));
        v = onView(withId(R.id.timerLayout));
        v = onView(withId(R.id.duration)).check(matches(withText("20")));
        click();
        isDisplayed();
        v.check(matches(isDisplayed());
    }

    private void initFragment(PlaybackTrackerFragment fragment) {
        HandlerThread worker = new HandlerThread("dsfsdf");
        worker.start();
        worker.getLooper().setMessageLogging((String x) -> {
            //Log.i(WORKER_ID, x);
        });
        MediaControllerAdapter mediaControllerAdapter = new MediaControllerAdapter(getApplicationContext(), worker.getLooper());
        fragment.init(mediaControllerAdapter);

    }

}