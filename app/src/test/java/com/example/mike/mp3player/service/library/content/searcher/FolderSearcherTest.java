package com.example.mike.mp3player.service.library.content.searcher;

import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.filter.FoldersResultFilter;
import com.example.mike.mp3player.service.library.search.Folder;
import com.example.mike.mp3player.service.library.search.FolderDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class FolderSearcherTest extends ContentResolverSearcherTestBase<FolderSearcher> {

    private FoldersResultFilter filter;

    @Mock
    FolderDao folderDao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.filter = mock(FoldersResultFilter.class);
        when(filter.filter(VALID_QUERY, expectedResult)).thenReturn(expectedResult);
        this.searcher = spy(new FolderSearcher(contentResolver, resultsParser, filter, idPrefix, folderDao));
    }

    @Override
    @Test
    public void testSearchValidMultipleArguments() {
        List<Folder> expectedDbResult = new ArrayList<>();
        final String id1 = "id1";
        final String id2 = "id2";
        final String id3 = "id3";
        final String value1 = "value1";
        final String value2 = "value2";
        final String value3 = "value3";
        Folder folder1 = new Folder(id1, value1);
        Folder folder2 = new Folder(id2, value2);
        Folder folder3 = new Folder(id3, value3);
        expectedDbResult.add(folder1);
        expectedDbResult.add(folder2);
        expectedDbResult.add(folder3);
        when(folderDao.query(VALID_QUERY)).thenReturn(expectedDbResult);

        final String EXPECTED_WHERE =
                MediaStore.Audio.Media.DATA + " LIKE ? OR "
                + MediaStore.Audio.Media.DATA + " LIKE ? OR "
                + MediaStore.Audio.Media.DATA + " LIKE ? COLLATE NOCASE";
        final String[] EXPECTED_WHERE_ARGS = new String[] {
                searcher.likeParam(id1),
                searcher.likeParam(id2),
                searcher.likeParam(id3)};
        when(contentResolver.query(EXTERNAL_CONTENT_URI, searcher.getProjection(), EXPECTED_WHERE, EXPECTED_WHERE_ARGS, null))
                .thenReturn(cursor);

        when(resultsParser.create(cursor, idPrefix)).thenReturn(expectedResult);
        List<MediaBrowserCompat.MediaItem> result = searcher.search(VALID_QUERY);
        assertEquals(expectedResult, result);
    }

    @Test
    @Override
    public void testGetMediaType() {
        assertEquals(MediaItemType.FOLDERS, searcher.getSearchCategory());
    }





}