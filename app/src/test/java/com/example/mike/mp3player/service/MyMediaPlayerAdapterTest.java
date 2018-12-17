package com.example.mike.mp3player.service;

import android.content.Context;
import android.net.Uri;
;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class MyMediaPlayerAdapterTest {

    @Mock
    Context context;
    @Mock
    Uri uri;

    private MyMediaPlayerAdapter mediaPlayerAdapter;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        mediaPlayerAdapter = createMediaPlayerAdapter();
    }

    @Test
    public void initTest() {
        mediaPlayerAdapter.init(uri);
        assertNotNull("MediaPlayer should not be null after initialisation", mediaPlayerAdapter.getMediaPlayer());
    }

    private MyMediaPlayerAdapter createMediaPlayerAdapter() {
        return new MyMediaPlayerAdapter(context);
    }

}