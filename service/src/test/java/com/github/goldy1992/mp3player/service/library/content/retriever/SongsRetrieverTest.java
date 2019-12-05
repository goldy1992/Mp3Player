package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;
import com.github.goldy1992.mp3player.service.library.search.Song;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static android.os.Looper.getMainLooper;
import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
import static com.github.goldy1992.mp3player.commons.MediaItemType.SONG;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SongsRetrieverTest extends ContentResolverRetrieverTestBase<SongsRetriever> {

    @Mock
    SongResultsParser resultsParser;

    @Captor
    ArgumentCaptor<List<Song>> captor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.retriever = spy(new SongsRetriever(contentResolver, resultsParser, handler));
    }

    @Test
    public void testGetChildren() {
        this.contentRequest = new ContentRequest("x", "y", "z");
        final String id = "xyz";
        final String title = "title";
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder(id)
                .setTitle(title)
                .build();
        expectedResult.add(mediaItem);
        when(contentResolver.query(EXTERNAL_CONTENT_URI, retriever.getProjection(), null, null, null)).thenReturn(cursor);
        when(resultsParser.create(cursor, contentRequest.getMediaIdPrefix())).thenReturn(expectedResult);


        List<MediaBrowserCompat.MediaItem> result = retriever.getChildren(contentRequest);
        // call remaining looper messages
        shadowOf(getMainLooper()).idle();

        // assert results are the expected ones
        assertEquals(expectedResult, result);
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(SONG, retriever.getType());
    }

}