package com.example.mike.mp3player.client.views.fragments;

import android.os.HandlerThread;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.mike.mp3player.CustomMatchers.withSeekbarProgress;
import static org.hamcrest.CoreMatchers.containsString;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class PlaybackTrackerFragmentTest {

    private final String HANDLER_THREAD_ID = "HANDLER_THREAD_ID";
    private long duration;
    private int position;
    private float playbackSpeed;
    @PlaybackStateCompat.State
    private int playbackState = STATE_PAUSED;

    FragmentScenario<PlaybackTrackerFragment> fragmentScenario;
    FragmentScenario.FragmentAction<PlaybackTrackerFragment> onMetadataChangedAction;
    FragmentScenario.FragmentAction<PlaybackTrackerFragment> onPlaybackStateChangedAction;
    @Before
    public void init(){
        FragmentFactory fragmentFactory = new FragmentFactory();
        fragmentScenario = FragmentScenario.launchInContainer(PlaybackTrackerFragment.class, null, R.style.AppTheme_NoActionBar, fragmentFactory);
        fragmentScenario.onFragment(fragment -> initFragment(fragment));
        onMetadataChangedAction = this::changeMetaData;
        onPlaybackStateChangedAction = (PlaybackTrackerFragment fragment) -> changePlaybackState(fragment);
    }

    /**
     * GIVEN an initialised instance of PlaybackTrackerFragment
     * WHEN: onMetadataChanged is called with a duration of twenty seconds
     * THEN: the duration TextView is updated accordingly.
     */
    @Test
    public void testDurationUpdated() {
        this.duration = 20000; // 20 seconds
        this.position = 10000;
        this.playbackState = STATE_PAUSED;
        this.playbackSpeed = 1.0f;
        final String EXPECTED = "20";
        fragmentScenario.onFragment(onMetadataChangedAction);

        onView(withId(R.id.duration)).check(matches(withText(containsString(EXPECTED))));
        fragmentScenario.onFragment(onPlaybackStateChangedAction);
        onView(withId(R.id.seekBar)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.seekBar)).check(matches(withSeekbarProgress(10000)));
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
        PlaybackStateCompat playbackStateCompat = new PlaybackStateCompat.Builder()
                .setState(playbackState, position, playbackSpeed).build();
        fragment.onPlaybackStateChanged(playbackStateCompat);
    }

}