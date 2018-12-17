package com.example.mike.mp3player.service;

import android.content.Context;
import android.net.Uri;
;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

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

    @org.junit.jupiter.api.Test
    public void initTest() {
        mediaPlayerAdapter.init(uri);
        assertNotNull("MediaPlayer should not be null after initialisation", mediaPlayerAdapter.getMediaPlayer());
    }

    private MyMediaPlayerAdapter createMediaPlayerAdapter() {
        return new MyMediaPlayerAdapter(context);
    }

}