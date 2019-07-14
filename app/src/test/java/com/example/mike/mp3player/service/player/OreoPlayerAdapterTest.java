package com.example.mike.mp3player.service.player;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OreoPlayerAdapterTest extends MediaPlayerAdapterTestBase {
    /**
     * setup
     */
    @Before
    public void setup() throws IllegalAccessException {
        super.setup();
    }

    @Override
    OreoPlayerAdapter createMediaPlayerAdapter() {
        OreoPlayerAdapter oreoPlayerAdapter = new OreoPlayerAdapter(context, audioFocusManager);
        oreoPlayerAdapter.setOnCompletionListener(mockOnCompletionListener);
        oreoPlayerAdapter.setOnSeekCompleteListener(mockOnSeekCompleteListener);
        return oreoPlayerAdapter;
    }
}