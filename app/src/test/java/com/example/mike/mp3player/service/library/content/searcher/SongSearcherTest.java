package com.example.mike.mp3player.service.library.content.searcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.commons.MediaItemType.SONGS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class SongSearcherTest extends ContentResolverSearcherTestBase<SongSearcher> {

    @Before
    public void setup() {
        super.setup();
        this.searcher = spy(new SongSearcher(contentResolver, resultsParser, idPrefix));
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(SONGS, searcher.getSearchCategory());
    }

}