package com.example.mike.mp3player.client.callbacks.playback;

import android.os.Handler;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MyPlaybackStateCallbackTest {

    private MyPlaybackStateCallback myPlaybackStateCallback;
    @Mock
    private Handler handler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.myPlaybackStateCallback = new MyPlaybackStateCallback(handler);
    }
}