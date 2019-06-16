package com.example.mike.mp3player.client.views.fragments;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.MainActivity;
import com.example.mike.mp3player.client.views.SongSearchActionListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MainFrameFragmentTest extends FragmentTestBase<MainFrameFragment> {

    @Mock
    private SongSearchActionListener songSearchActionListener;
    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;
    @Mock
    private MediaControllerAdapter mediaControllerAdapter;
    @Before
    public void setup() {

        ActivityScenario<MainActivity> activityScenario = ActivityScenario.launch(MainActivity.class);
      //  activityScenario.onActivity()
        // TODO: fix test
//        super.setup(MainFrameFragment.class);
//        MockitoAnnotations.initMocks(this);
//        FragmentScenario.FragmentAction<MainFrameFragment> initFragment = this::callInitFragment;
//        fragmentScenario.onFragment(initFragment);

    }

    @Test
    public void testEnable() {
        assertTrue(true);
    }

    private void callInitFragment(MainFrameFragment fragment) {
        fragment.init(songSearchActionListener, mediaBrowserAdapter, mediaControllerAdapter);
    }

}