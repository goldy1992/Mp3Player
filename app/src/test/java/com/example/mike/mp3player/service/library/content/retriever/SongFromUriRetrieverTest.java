package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.library.content.parser.SongResultsParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;
import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SongFromUriRetrieverTest {

    private SongFromUriRetriever songFromUriRetriever;

    @Mock
    private ContentResolver contentResolver;

    @Mock
    private SongResultsParser songResultsParser;

    @Mock
    private Uri testUri;

    @Mock
    private MediaMetadataRetriever mmr;

    private static final String ID = "id";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.songFromUriRetriever = new SongFromUriRetriever(context, contentResolver, songResultsParser, mmr, ID);
    }

    @Test
    public void testGetSongWithContentScheme() {
        when(testUri.getScheme()).thenReturn(ContentResolver.SCHEME_CONTENT);

        final byte[] expectedEmbeddedPic = new byte[1];
        when(mmr.getEmbeddedPicture()).thenReturn(expectedEmbeddedPic);

        final String expectedTitle = "TITLE";
        when(mmr.extractMetadata(METADATA_KEY_TITLE)).thenReturn(expectedTitle);

        final String expectedArtist = "ARTIST";
        when(mmr.extractMetadata(METADATA_KEY_ARTIST)).thenReturn(expectedArtist);

        final long expectedDuration = 1123L;
        when(mmr.extractMetadata(METADATA_KEY_DURATION)).thenReturn(String.valueOf(expectedDuration));


        MediaItem result = songFromUriRetriever.getSong(testUri);

        final byte[] actualEmbeddedPicture = MediaItemUtils.getAlbumArtImage(result);
        assertEquals(expectedEmbeddedPic, actualEmbeddedPicture);

        final String actualTitle = MediaItemUtils.getTitle(result);
        assertEquals(expectedTitle, actualTitle);

        final String actualArtist = MediaItemUtils.getArtist(result);
        assertEquals(expectedArtist, actualArtist);

        final long actualDuration = MediaItemUtils.getDuration(result);
        assertEquals(expectedDuration, actualDuration);

        final Uri actualMediaUri = MediaItemUtils.getMediaUri(result);
        assertEquals(testUri, actualMediaUri);
    }

    @Test
    public void testGetSongWithNonContentScheme() {

        final MediaItem expectedMediaItem = mock(MediaItem.class);
        Cursor cursor = mock(Cursor.class);
        when(contentResolver.query(any(), any(), any(), any(), any()))
                .thenReturn(cursor);

        when(songResultsParser.create(cursor, ID)).thenReturn(Collections.singletonList(expectedMediaItem));
        when(testUri.getScheme()).thenReturn(ContentResolver.SCHEME_FILE);

        MediaItem result = songFromUriRetriever.getSong(testUri);

        assertEquals(expectedMediaItem, result);

    }

    @Test
    public void testUriWithNullScheme() {
        when(testUri.getScheme()).thenReturn(null);
        MediaItem result = songFromUriRetriever.getSong(testUri);
        assertNull(result);
    }
}