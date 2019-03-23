package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.example.mike.mp3player.service.player.MyMediaPlayerAdapter;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

public class MediaPlayerAdapterTestBase {

    @Mock
    Context context;
    @Mock
    MediaPlayer mediaPlayer;
    @Mock
    Uri uri;
    @Mock
    Uri nextUri;
    @Mock
    AudioFocusManager audioFocusManager;

    MyMediaPlayerAdapter mediaPlayerAdapter;

    public void setup() {
        MockitoAnnotations.initMocks(this);
        mediaPlayerAdapter = createMediaPlayerAdapter();
        when(MediaPlayer.create(any(Context.class), any(Uri.class))).thenReturn(mediaPlayer);
        mediaPlayerAdapter.reset(uri, null);
        Whitebox.setInternalState(mediaPlayerAdapter, "audioFocusManager", audioFocusManager);
//      //  assertNotNull("MediaPlayer should not be null after initialisation", mediaPlayerAdapter.getCurrentMediaPlayer());
//        assertTrue("Didn't initialise MediaPlayerAdapter correctly", mediaPlayerAdapter.isPrepared());
//        assertEquals("Initialised into the incorrect state", PlaybackStateCompat.STATE_PAUSED, mediaPlayerAdapter.getCurrentState());
    }


    protected MyMediaPlayerAdapter createMediaPlayerAdapter() {
        return new MyMediaPlayerAdapter(this.context, null, null);
    }

    protected void setMediaPlayer(MediaPlayer mediaPlayer) {
        Whitebox.setInternalState(mediaPlayerAdapter, "mediaPlayer", mediaPlayer);
    }

}
