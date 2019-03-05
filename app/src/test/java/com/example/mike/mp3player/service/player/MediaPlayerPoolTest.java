package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.os.Looper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class MediaPlayerPoolTest {

    @Mock
    private Context context;
    @Mock
    private Looper looper;

    private MediaPlayerPool m_mediaPlayerPool;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        m_mediaPlayerPool = new MediaPlayerPool(context);
    }

    @AfterEach
    void tearDown() {
    }

}