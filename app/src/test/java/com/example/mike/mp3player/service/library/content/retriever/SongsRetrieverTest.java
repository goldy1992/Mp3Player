package com.example.mike.mp3player.service.library.content.retriever;

import android.util.Log;

import com.example.mike.mp3player.service.library.search.SongDao;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.text.Normalizer;

import static com.example.mike.mp3player.commons.MediaItemType.SONG;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class SongsRetrieverTest extends ContentResolverRetrieverTestBase<SongsRetriever> {

    @Mock
    SongDao songDao;

    @Before
    public void setup() {
        super.setup();
        this.retriever = spy(new SongsRetriever(contentResolver, resultsParser, songDao, handler));
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(SONG, retriever.getType());
    }

}