package com.example.mike.mp3player.client.views.fragments;

import android.media.session.PlaybackState;
import android.os.HandlerThread;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class PlaybackTrackerFragmentTest {

    private final String HANDLER_THREAD_ID = "HANDLER_THREAD_ID";
    private long duration;
    private int position;
    private float playbackSpeed;
    FragmentScenario<PlaybackTrackerFragment> fragmentScenario;
    FragmentScenario.FragmentAction<PlaybackTrackerFragment> onMetadataChangedAction;
    @Before
    public void init(){
        fragmentScenario = FragmentScenario.launchInContainer(PlaybackTrackerFragment.class);
        fragmentScenario.moveToState(Lifecycle.State.CREATED);
        fragmentScenario.onFragment(fragment -> initFragment(fragment));
        fragmentScenario.moveToState(Lifecycle.State.STARTED);
        fragmentScenario.moveToState(Lifecycle.State.RESUMED);
        onMetadataChangedAction = (PlaybackTrackerFragment fragment) -> changeMetaData(fragment);
    }

    /**
     * GIVEN an initialised instance of PlaybackTrackerFragment
     * WHEN: onMetadataChanged is called with a duration of twenty seconds
     * THEN: the duration TextView is updated accordingly.
     */
    @Test
    public void testDurationUpdated() {
        this.duration = 20000; // 20 seconds
        final String EXPECTED = "20";
        fragmentScenario.onFragment(onMetadataChangedAction);
        onView(withId(R.id.duration)).check(
                matches(withText(containsString(EXPECTED))));
    }
    /**
     *
     * @param fragment
     */
    private void initFragment(PlaybackTrackerFragment fragment) {
        HandlerThread worker = new HandlerThread(HANDLER_THREAD_ID);
        worker.start();
        MediaControllerAdapter mediaControllerAdapter = new MediaControllerAdapter(getApplicationContext(), worker.getLooper());
        fragment.init(mediaControllerAdapter);
    }

    private void changeMetaData(@NonNull PlaybackTrackerFragment fragment){
        MediaMetadataCompat metadataCompat = new MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, this.duration).build();
        fragment.onMetadataChanged(metadataCompat);
    }

    private void changePlaybackState(@NonNull PlaybackTrackerFragment fragment){
        PlaybackStateCompat playbackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, position, playbackSpeed).build();
        fragment.onPlaybackStateChanged(playbackState);
    }

}