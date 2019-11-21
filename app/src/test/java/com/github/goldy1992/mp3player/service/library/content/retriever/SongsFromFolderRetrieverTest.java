package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static android.os.Looper.getMainLooper;
import static com.github.goldy1992.mp3player.commons.MediaItemType.SONG;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SongsFromFolderRetrieverTest extends ContentResolverRetrieverTestBase<SongsFromFolderRetriever> {

    @Mock
    SongResultsParser resultsParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.retriever = spy(new SongsFromFolderRetriever(contentResolver, resultsParser, null, handler));
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
        when(contentResolver.query(any(), any(), any(), any(), eq(null))).thenReturn(cursor);
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