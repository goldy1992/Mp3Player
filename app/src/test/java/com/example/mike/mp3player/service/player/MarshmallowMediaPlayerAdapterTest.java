package com.example.mike.mp3player.service.player;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MarshmallowMediaPlayerAdapterTest extends MediaPlayerAdapterTestBase {

    /** {@inheritDoc} */
    @Before
    public void setup() throws IllegalAccessException {
       super.setup();
    }

    @Override
    MarshmallowMediaPlayerAdapter createMediaPlayerAdapter() {
        MarshmallowMediaPlayerAdapter marshmallowMediaPlayerAdapter = new MarshmallowMediaPlayerAdapter(context, audioFocusManager);
        marshmallowMediaPlayerAdapter.setOnCompletionListener(mockOnCompletionListener);
        marshmallowMediaPlayerAdapter.setOnSeekCompleteListener(mockOnSeekCompleteListener);
        return marshmallowMediaPlayerAdapter;
    }
}