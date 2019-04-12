package com.example.mike.mp3player.service.session;

import android.media.PlaybackParams;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import static com.example.mike.mp3player.commons.Constants.NO_ACTION;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MediaSessionAdapterTest {

    @Mock
    MediaPlayerAdapterBase mediaPlayerAdapter;
    @Mock
    PlaybackManager playbackManager;
    @Mock
    MediaSessionCompat mediaSession;

    MediaSessionAdapter mediaSessionAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mediaSessionAdapter =new MediaSessionAdapter(mediaSession, playbackManager, mediaPlayerAdapter);
    }

    @Test
    public void getMediaPlayerStateBuilder() {
        final int EXPECTED_STATE = PlaybackStateCompat.STATE_BUFFERING;
        final int EXPECTED_CURRENT_POSITION = 1234;
        final float EXPECTED_SPEED = 123f;

        when(mediaPlayerAdapter.getCurrentPosition()).thenReturn(EXPECTED_CURRENT_POSITION);
        PlaybackParams playbackParams = new PlaybackParams();
        Whitebox.setInternalState(mediaPlayerAdapter, "currentState", EXPECTED_STATE);

        PlaybackStateCompat result = mediaSessionAdapter.getCurrentPlaybackState(NO_ACTION);
        long resultPosition = result.getPosition();
        float resultSpeed = result.getPlaybackSpeed();
        float speedDiff = EXPECTED_SPEED - resultSpeed;
        int resultState = result.getState();
        assertEquals(EXPECTED_CURRENT_POSITION, (int)resultPosition);
    }

}