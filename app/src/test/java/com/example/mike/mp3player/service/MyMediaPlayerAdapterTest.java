package com.example.mike.mp3player.service;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MyMediaPlayerAdapterTest {

    @Mock
    Context context;
    @Mock
    Uri uri;

    private MyMediaPlayerAdapter mediaPlayerAdapter;

    @BeforeEach
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        mediaPlayerAdapter = createMediaPlayerAdapter();
    }

    @Test
    public void initTest() {
        mediaPlayerAdapter.init(uri);
        assertNotNull("MediaPlayer should not be null after initialisation", mediaPlayerAdapter.getMediaPlayer());
        assertTrue("Didn't initialise MediaPlayerAdapter correctly", mediaPlayerAdapter.isPrepared());
        assertEquals("Initialised into the incorrect state", PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
    }

    private MyMediaPlayerAdapter createMediaPlayerAdapter() {
        return new MyMediaPlayerAdapter(context);
    }

}