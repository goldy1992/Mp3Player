package com.example.mike.mp3player.client.views.fragments;

import android.util.Log;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import com.example.mike.mp3player.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
@MediumTest
@RunWith(AndroidJUnit4.class)

public class TestSeekBarFragmentTest {

    private final String HANDLER_THREAD_ID = "HANDLER_THREAD_ID";

    FragmentScenario<TestSeekBarFragment> fragmentScenario;

    @Before
    public void init() {
        FragmentFactory fragmentFactory = new FragmentFactory();
        fragmentScenario = FragmentScenario.launchInContainer(TestSeekBarFragment.class, null, R.style.AppTheme_NoActionBar, fragmentFactory);

    }

    /**
     * GIVEN an initialised instance of PlaybackTrackerFragment
     * WHEN: onMetadataChanged is called with a duration of twenty seconds
     * THEN: the duration TextView is updated accordingly.
     */
    @Test
    public void testDurationUpdated() {
        Log.i(",", "");
    }

}
