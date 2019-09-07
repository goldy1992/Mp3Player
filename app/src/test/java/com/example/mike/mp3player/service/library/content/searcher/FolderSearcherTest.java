package com.example.mike.mp3player.service.library.content.searcher;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.filter.FoldersResultFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class FolderSearcherTest extends ContentResolverSearcherTestBase<FolderSearcher> {

    private FoldersResultFilter filter;

    @Before
    public void setup() {
        super.setup();
        this.filter = mock(FoldersResultFilter.class);
        when(filter.filter(VALID_QUERY, expectedResult)).thenReturn(expectedResult);
        this.searcher = spy(new FolderSearcher(contentResolver, resultsParser, filter, idPrefix));
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(MediaItemType.FOLDERS, searcher.getSearchCategory());
    }



}