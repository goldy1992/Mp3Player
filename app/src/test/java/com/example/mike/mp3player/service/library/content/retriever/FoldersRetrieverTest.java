package com.example.mike.mp3player.service.library.content.retriever;

import com.example.mike.mp3player.service.library.search.FolderDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.commons.MediaItemType.FOLDER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class FoldersRetrieverTest extends ContentResolverRetrieverTestBase<FoldersRetriever> {

    @Mock
    FolderDao folderDao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup();
        this.retriever = spy(new FoldersRetriever(contentResolver, resultsParser, folderDao, handler));
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(FOLDER, retriever.getType());
    }

}