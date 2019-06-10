package com.example.mike.mp3player.service.session;

import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.session.MediaSessionCompat.QueueItem;
import static com.example.mike.mp3player.commons.Constants.NO_ACTION;
import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MediaSessionAdapterTest {
    private final static String ID = "ID";

    @Mock
    MediaPlayerAdapterBase mediaPlayerAdapter;
    @Mock
    PlaybackManager playbackManager;
    @Spy
    MediaSessionCompat mediaSession = new MediaSessionCompat(InstrumentationRegistry.getInstrumentation().getContext(), "M_SESSION");
    MediaSessionAdapter mediaSessionAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mediaSessionAdapter =new MediaSessionAdapter(mediaSession, playbackManager, mediaPlayerAdapter);
    }

    @Test
    public void getMediaPlayerStateBuilder() throws IllegalAccessException {
        final int EXPECTED_STATE = PlaybackStateCompat.STATE_BUFFERING;
        final int EXPECTED_CURRENT_POSITION = 1234;
        final float EXPECTED_SPEED = 123f;

        when(mediaPlayerAdapter.getCurrentPosition()).thenReturn(EXPECTED_CURRENT_POSITION);
        FieldUtils.writeField(mediaPlayerAdapter, "currentState", EXPECTED_STATE, true);

        PlaybackStateCompat result = mediaSessionAdapter.getCurrentPlaybackState(NO_ACTION);
        long resultPosition = result.getPosition();
        float resultSpeed = result.getPlaybackSpeed();
        float speedDiff = EXPECTED_SPEED - resultSpeed;
        int resultState = result.getState();
        assertEquals(EXPECTED_CURRENT_POSITION, (int)resultPosition);
    }

    /**
     * Test to see that correct metadata is returned
     */
    @Test
    public void testGetCurrentMetaDataWithValidData() {

        final String title = "title";
        final String artist = "artist";
        QueueItem queueItem = createQueueItem(ID, title, artist);
        when(playbackManager.getCurrentItem()).thenReturn(queueItem);
        MediaMetadataCompat mediaMetadata = mediaSessionAdapter.getCurrentMetaData();
        // assert correct ID is returned
        assertTrue(mediaMetadata.containsKey(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));
        assertEquals(ID, mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID));
        // assert correct title is returned
        assertTrue(mediaMetadata.containsKey(MediaMetadataCompat.METADATA_KEY_TITLE));
        assertEquals(title, mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        // assert correct artist is returned
        assertTrue(mediaMetadata.containsKey(MediaMetadataCompat.METADATA_KEY_ARTIST));
        assertEquals(artist, mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
    }
    /**
     * Test to see that correct metadata is returned
     */
    @Test
    public void testGetCurrentMetaDataWithInvalidData() {
        QueueItem queueItem = createQueueItem(ID, null, null);
        when(playbackManager.getCurrentItem()).thenReturn(queueItem);
        MediaMetadataCompat mediaMetadata = mediaSessionAdapter.getCurrentMetaData();
        // assert UNKNOWN is returned for title
        assertTrue(mediaMetadata.containsKey(MediaMetadataCompat.METADATA_KEY_TITLE));
        assertEquals(UNKNOWN, mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        // assert UNKNOWN artist is returned for artist
        assertTrue(mediaMetadata.containsKey(MediaMetadataCompat.METADATA_KEY_ARTIST));
        assertEquals(UNKNOWN, mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
    }
    private QueueItem createQueueItem(String id, String title, String artist) {
        Bundle extras = new Bundle();
        extras.putString(STRING_METADATA_KEY_ARTIST, artist);
        MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
                .setMediaId(id)
                .setTitle(title)
                .setExtras(extras)
                .build();
        return new QueueItem(description, 0L);
    }
    @Test
    public void testUpdateAll() {
        mediaSessionAdapter.updateAll();
        verify(mediaSession, times(1)).setPlaybackState(any());
        verify(mediaSession, times(1)).setMetadata(any());
    }
    @Test
    public void testSetQueue() {
        QueueItem queueItem = createQueueItem(ID, null, null);
        mediaSessionAdapter.setQueue(queueItem);
        verify(mediaSession, times(1)).setQueue(any());
    }
    @Test
    public void testSetActive() {
        final boolean isActive = true;
        mediaSessionAdapter.setActive(isActive);
        verify(mediaSession, times(1)).setActive(isActive);
    }

}