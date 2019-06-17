package com.example.mike.mp3player.client.views.fragments;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MainFragmentTest2 extends FragmentTestBase<MainFrameFragment> {

    @Before
    public void setup() {
        super.setup(MainFrameFragment.class);
    }

    @Test
    public void testTrue() {
        assertTrue(true);
    }
}
