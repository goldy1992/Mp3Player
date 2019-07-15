package com.example.mike.mp3player.client;

import android.content.Context;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MyMetaDataCallback;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;
import com.example.mike.mp3player.shadows.ShadowMediaSessionCompat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

@RunWith(RobolectricTestRunner.class)
public class MediaControllerAdapterTest {

    private MediaControllerAdapter mediaControllerAdapter;

    @Before
    public void setup() {
        Handler handler = mock(Handler.class);
        MyMetaDataCallback myMetaDataCallback = new MyMetaDataCallback(handler);
        MyPlaybackStateCallback myPlaybackStateCallback = new MyPlaybackStateCallback(handler);
        MyMediaControllerCallback myMediaControllerCallback = new MyMediaControllerCallback(myMetaDataCallback, myPlaybackStateCallback);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        mediaControllerAdapter = new MediaControllerAdapter(context, myMediaControllerCallback);
        assertFalse(mediaControllerAdapter.isInitialized());
    }

    @Test
    public void testSetMediaToken() {

//        MediaSessionCompat.Token token = mock(MediaSessionCompat.Token.class, withSettings());
//        mediaControllerAdapter.setMediaToken(token);
    }
    @Test
    public void firstTest() {
        assertTrue(true);
    }
}