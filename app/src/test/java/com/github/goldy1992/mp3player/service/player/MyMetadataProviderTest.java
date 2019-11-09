package com.github.goldy1992.mp3player.service.player;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaMetadataCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.service.PlaybackManager;
import com.google.android.exoplayer2.ExoPlayer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MyMetadataProviderTest {

    @Mock
    private PlaybackManager playbackManager;
    @Mock
    private ExoPlayer exoPlayer;

    private MyMetadataProvider myMetadataProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.myMetadataProvider = new MyMetadataProvider(playbackManager);
    }

    @Test
    public void testGetMetadata() {
        final String expectedId = "id";
        final String expectedTitle = "title";
        final String expectedArtist = "artist";
        final String expectedAlbumArt = "/a/b/albumart";
        final long expectedDuration = 342L;
        MediaItem mediaItem = new MediaItemBuilder(expectedId)
                .setDuration(expectedDuration)
                .setTitle(expectedTitle)
                .setArtist(expectedArtist)
                .setAlbumArtUri(Uri.parse(expectedAlbumArt))
                .build();
        final int index = 7;
        when(exoPlayer.getCurrentWindowIndex()).thenReturn(index);
        when(playbackManager.getItemAtIndex(index)).thenReturn(mediaItem);
        MediaMetadataCompat result = myMetadataProvider.getMetadata(exoPlayer);

        final String actualId = result.getString(METADATA_KEY_MEDIA_ID);
        assertEquals(expectedId, actualId);

        final long actualDuration = result.getLong(METADATA_KEY_DURATION);
        assertEquals(expectedDuration, actualDuration);

        final String actualTitle = result.getString(METADATA_KEY_TITLE);
        assertEquals(expectedTitle, actualTitle);

        final String actualArtist = result.getString(METADATA_KEY_ARTIST);
        assertEquals(expectedArtist, actualArtist);

        final String actualAlbumArt = result.getString(METADATA_KEY_ALBUM_ART_URI);
        assertEquals(expectedAlbumArt, actualAlbumArt);
    }

}