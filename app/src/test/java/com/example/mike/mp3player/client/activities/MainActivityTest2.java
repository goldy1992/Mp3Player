package com.example.mike.mp3player.client.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest2 {

    @Before
    public void setup() {
        Robolectric.buildActivity(MainActivity.class);
    }

    @Test
    public void testTrue() {
        assertTrue(true);
    }

}
