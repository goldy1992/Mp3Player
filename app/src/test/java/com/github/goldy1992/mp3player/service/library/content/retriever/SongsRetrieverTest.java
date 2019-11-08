package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;
import com.github.goldy1992.mp3player.service.library.search.Song;
import com.github.goldy1992.mp3player.service.library.search.SongDao;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SongsRetrieverTest extends ContentResolverRetrieverTestBase<SongsRetriever> {

    @Mock
    SongDao songDao;

    @Captor
    ArgumentCaptor<List<Song>> captor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.retriever = spy(new SongsRetriever(contentResolver, resultsParser, songDao, handler));
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
        /* IN ORDER for the database update code to be hit, there needs to be difference in file
        numbers to call it. This is a flaw in the design and will be addressed in another issue

         TO call the code we therefore differe the result size */
        when(songDao.getCount()).thenReturn(expectedResult.size() + 1);


        List<MediaBrowserCompat.MediaItem> result = retriever.getChildren(contentRequest);
        // call remaining looper messages
        shadowOf(getMainLooper()).idle();

        // assert results are the expected ones
        assertEquals(expectedResult, result);
        // verify database call
        verify(songDao, times(1)).insertAll(captor.capture());
        List<Song> songs = captor.getValue();
        assertEquals(1, songs.size());
        Song song = songs.get(0);
        assertEquals(song.getId(), id);
        assertEquals(song.getValue(), title);
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(SONG, retriever.getType());
    }

}