package com.example.mike.mp3player.client;

import android.content.Context;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MyMetaDataCallback;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MediaControllerAdapterTest {

    private MediaControllerAdapter mediaControllerAdapter;
    @Mock
    private MyMetaDataCallback myMetaDataCallback;
    @Mock
    private MyPlaybackStateCallback playbackStateCallback;

    private MyMediaControllerCallback myMediaControllerCallback;
    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.myMediaControllerCallback = spy(new MyMediaControllerCallback(myMetaDataCallback, playbackStateCallback));

        MediaSessionCompat.Token token = getMediaSessionCompatToken();
        this.mediaControllerAdapter = initialiseMediaControllerAdapter(token);
    }

    @Test
    public void testIsInitialised() {
        when(mediaControllerAdapter.getMediaController().isSessionReady()).thenReturn(true);
        assertTrue(mediaControllerAdapter.isInitialized());
    }

    @Test
    public void testSetMediaSessionTokenWhenAlreadyInitialised() {
        when(mediaControllerAdapter.isInitialized()).thenReturn(true);
        MediaSessionCompat.Token token = getMediaSessionCompatToken();
        reset(mediaControllerAdapter);
        mediaControllerAdapter.setMediaToken(token);
        verify(mediaControllerAdapter, never()).init(token);
    }

    @Test
    public void testPlay() {
        mediaControllerAdapter.play();
        verify(mediaControllerAdapter, times(1)).play();
    }

    @Test
    public void testPause() {
        mediaControllerAdapter.pause();
        verify(mediaControllerAdapter, times(1)).pause();
    }

    @Test
    public void testSkipToNext() {
        mediaControllerAdapter.skipToNext();
        verify(mediaControllerAdapter, times(1)).skipToNext();
    }

    @Test
    public void testSkipToPrevious() {
        mediaControllerAdapter.skipToPrevious();
        verify(mediaControllerAdapter, times(1)).skipToPrevious();
    }

    @Test
    public void testStop() {
        mediaControllerAdapter.stop();
        verify(mediaControllerAdapter, times(1)).stop();
    }

    @Test
    public void testPrepareFromMediaId() {
        final String mediaId = "MEDIA_ID";
        final Bundle extras = new Bundle();
        mediaControllerAdapter.prepareFromMediaId(mediaId, extras);
        verify(mediaControllerAdapter, times(1)).prepareFromMediaId(mediaId, extras);
    }

    @Test
    public void testSeekTo() {
        final long position = 23542L;
        mediaControllerAdapter.seekTo(position);
        verify(mediaControllerAdapter, times(1)).seekTo(position);
    }

    @Test
    public void testRepeatMode() {
        @PlaybackStateCompat.State final int repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL;
        mediaControllerAdapter.setRepeatMode(repeatMode);
        verify(mediaControllerAdapter, times(1)).setRepeatMode(repeatMode);
    }

    @Test
    public void testSendCustomAction() {
        final String customAction = "DO_SOMETHING";
        final Bundle args = new Bundle();
        mediaControllerAdapter.sendCustomAction(customAction, args);
        verify(mediaControllerAdapter, times(1)).sendCustomAction(customAction, args);
    }

    @Test
    public void testShuffleMode() {
        @PlaybackStateCompat.State final int shuffleMode = PlaybackStateCompat.SHUFFLE_MODE_ALL;
        mediaControllerAdapter.setShuffleMode(shuffleMode);
        verify(mediaControllerAdapter, times(1)).setShuffleMode(shuffleMode);
    }

    @Test
    public void testNullGetPlaybackState() {
        int result = mediaControllerAdapter.getPlaybackState();
        assertEquals(0, result);
    }

    @Test
    public void testGetPlaybackState() {
        @PlaybackStateCompat.State final int state = PlaybackStateCompat.STATE_PAUSED;
        final PlaybackStateCompat expectedState = new PlaybackStateCompat.Builder()
                .setState(state, 34L, 0.4f)
                .build();

        MediaControllerCompat mediaControllerCompat = mediaControllerAdapter.getMediaController();
        when(mediaControllerCompat.getPlaybackState()).thenReturn(expectedState);
        @PlaybackStateCompat.State final int result = mediaControllerAdapter.getPlaybackState();
        assertEquals(state, result);
    }

    @Test
    public void testRegisterMetaDataListener() {
        MetaDataListener expected = mock(MetaDataListener.class);
        mediaControllerAdapter.registerMetaDataListener(expected);
        verify(myMetaDataCallback, times(1)).registerMetaDataListener(expected);
    }

    @Test
    public void testUnregisterMetaDataListener() {
        MetaDataListener expected = mock(MetaDataListener.class);
        mediaControllerAdapter.unregisterMetaDataListener(expected);
        verify(myMetaDataCallback, times(1)).removeMetaDataListener(expected);
    }

    @Test
    public void testUnregisterPlaybackStateListener() {
        PlaybackStateListener expected = mock(PlaybackStateListener.class);
        mediaControllerAdapter.unregisterPlaybackStateListener(expected);
        verify(playbackStateCallback, times(1)).removePlaybackStateListener(expected);
    }

    @Test
    public void testRegisterPlaybackListener() {
        PlaybackStateListener expected = mock(PlaybackStateListener.class);
        Set<ListenerType> listenerTypeSet = Collections.singleton(ListenerType.PLAYBACK);
        mediaControllerAdapter.registerPlaybackStateListener(expected, listenerTypeSet);
        verify(playbackStateCallback, times(1)).registerPlaybackStateListener(expected, listenerTypeSet);
    }

    @Test
    public void testDisconnect() {
        mediaControllerAdapter.disconnect();
        verify(mediaControllerAdapter.getMediaController(), times(1)).unregisterCallback(myMediaControllerCallback);
    }

    @Test
    public void testUpdateUiStateWhenInitialised() {
        when(mediaControllerAdapter.isInitialized()).thenReturn(true);
        MediaControllerCompat mediaController = mediaControllerAdapter.getMediaController();
        final MediaMetadataCompat expectedMetadataCompat = mediaController.getMetadata();
        final PlaybackStateCompat expectedPlaybackStateCompat = mediaController.getPlaybackState();
        mediaControllerAdapter.updateUiState();
        verify(myMediaControllerCallback).onMetadataChanged(expectedMetadataCompat);
        verify(playbackStateCallback).updateAll(expectedPlaybackStateCompat);
    }

    @Test
    public void testUpdateUiStateWhenNotInitialised() {
        when(mediaControllerAdapter.isInitialized()).thenReturn(false);
        MediaControllerCompat mediaController = mediaControllerAdapter.getMediaController();
        final MediaMetadataCompat expectedMetadataCompat = mediaController.getMetadata();
        final PlaybackStateCompat expectedPlaybackStateCompat = mediaController.getPlaybackState();
        mediaControllerAdapter.updateUiState();
        verify(myMediaControllerCallback, never()).onMetadataChanged(expectedMetadataCompat);
        verify(myMediaControllerCallback, never()).onPlaybackStateChanged(expectedPlaybackStateCompat);
    }


    private MediaControllerAdapter initialiseMediaControllerAdapter(MediaSessionCompat.Token token) {
             MediaControllerAdapter spiedMediaControllerAdapter = spy(new MediaControllerAdapter(context, myMediaControllerCallback));
        assertFalse(spiedMediaControllerAdapter.isInitialized());
        spiedMediaControllerAdapter.setMediaToken(token);
        assertEquals(token, spiedMediaControllerAdapter.getToken());
        MediaControllerCompat.TransportControls transportControls = mock(MediaControllerCompat.TransportControls.class);
        when(spiedMediaControllerAdapter.getController()).thenReturn(transportControls);
        MediaControllerCompat mediaControllerCompat = mock(MediaControllerCompat.class);
        spiedMediaControllerAdapter.setMediaController(mediaControllerCompat);
        return spiedMediaControllerAdapter;
    }
    private MediaSessionCompat.Token getMediaSessionCompatToken() {
        MediaSession mediaSession = new MediaSession(InstrumentationRegistry.getInstrumentation().getContext(), "sd");
        MediaSession.Token sessionToken = mediaSession.getSessionToken();
        return MediaSessionCompat.Token.fromToken(sessionToken);
    }

}