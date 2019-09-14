package com.example.mike.mp3player.service.library.content.retriever;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.commons.MediaItemType.SONG;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class SongsFromFolderRetrieverTest extends ContentResolverRetrieverTestBase<SongsFromFolderRetriever> {

    @Before
    public void setup() {
        super.setup();
        this.retriever = spy(new SongsFromFolderRetriever(contentResolver, resultsParser, null, handler));
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(SONG, retriever.getType());
    }
}