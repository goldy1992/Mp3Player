package com.example.mike.mp3player.client.views.fragments;

import android.os.HandlerThread;
import android.support.v4.media.MediaMetadataCompat;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
@MediumTest
@RunWith(AndroidJUnit4.class)
public class PlaybackTrackerFragmentTest {

    FragmentScenario<PlaybackTrackerFragment> fragmentScenario;
    FragmentScenario.FragmentAction<PlaybackTrackerFragment> playbackTrackerFragmentFragmentAction;
    @Before
    public void init(){
       fragmentScenario =
               FragmentScenario.launchInContainer(PlaybackTrackerFragment.class);
       fragmentScenario.moveToState(Lifecycle.State.CREATED);
       fragmentScenario.onFragment(fragment -> initFragment(fragment));
        fragmentScenario.moveToState(Lifecycle.State.STARTED);
       playbackTrackerFragmentFragmentAction = (PlaybackTrackerFragment fragment) -> changeMetaData(fragment);
    }

    @Test
    public void firstTest() {

        fragmentScenario.onFragment(playbackTrackerFragmentFragmentAction);

        //InstrumentationRegistry.getInstrumentation().get
        onView(withChild(withId(R.id.decor_content_parent))).perform(click());
//                allOf(withChild( allOf(withId(R.id.timerLayout))), withChild(withId(R.id.duration))))

       // click();
     //   isDisplayed();
   //     v.check(matches(isDisplayed());
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

    private void changeMetaData(@NonNull PlaybackTrackerFragment fragment){
        MediaMetadataCompat m=new MediaMetadataCompat.Builder().putLong(MediaMetadataCompat.METADATA_KEY_DURATION,20000).build();
        fragment.onMetadataChanged(m);
        }
}