package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;

import org.mockito.Mock;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MediaPlayerAdapterTestBase {

    @Mock
    Context context;
    @Mock
    Uri uri;
    @Mock
    AudioFocusManager audioFocusManager;

    MyMediaPlayerAdapter mediaPlayerAdapter;

    public void setup() {
        mediaPlayerAdapter = createMediaPlayerAdapter();
        mediaPlayerAdapter.init(uri);
        Whitebox.setInternalState(mediaPlayerAdapter, "audioFocusManager", audioFocusManager);
      //  assertNotNull("MediaPlayer should not be null after initialisation", mediaPlayerAdapter.getMediaPlayer());
        assertTrue("Didn't initialise MediaPlayerAdapter correctly", mediaPlayerAdapter.isPrepared());
        assertEquals("Initialised into the incorrect state", PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
    }

    protected MyMediaPlayerAdapter createMediaPlayerAdapter() {
        return new MyMediaPlayerAdapter(context);
    }

    protected void setMediaPlayer(MediaPlayer mediaPlayer) {
        Whitebox.setInternalState(mediaPlayerAdapter, "mediaPlayer", mediaPlayer);
    }

}
