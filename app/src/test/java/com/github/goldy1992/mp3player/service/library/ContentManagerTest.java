package com.github.goldy1992.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.content.ContentRetrievers;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequestParser;
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentRetriever;
import com.github.goldy1992.mp3player.service.library.content.retriever.RootRetriever;
import com.github.goldy1992.mp3player.service.library.content.retriever.SongFromUriRetriever;
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ContentManagerTest {

    private static final String VALID_QUERY = "QUERY";

    private static final String LOWER_CASE_VALID_QUERY = "query";

    private ContentManager contentManager;

    @Mock
    private ContentRequestParser contentRequestParser;

    @Mock
    private ContentRetrievers contentRetrievers;

    @Mock
    private ContentSearchers contentSearchers;

    @Mock
    private RootRetriever rootRetriever;
    @Mock
    private MediaItem rootItem;

    @Mock
    private SongFromUriRetriever songFromUriRetriever;

    private static Map<MediaItemType, ContentSearcher> contentSearcherMap;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(contentRetrievers.getRoot()).thenReturn(rootRetriever);
        when(rootRetriever.getRootItem(any())).thenReturn(rootItem);
        this.contentManager = new ContentManager(contentRetrievers,
                contentSearchers,
                contentRequestParser,
                songFromUriRetriever);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetChildren() {
        final String contentRetrieverId = "id";
        final List<MediaItem> expectedList = new ArrayList<>();
        final ContentRetriever contentRetriever = mock(ContentRetriever.class);
        when(contentRetrievers.get(contentRetrieverId)).thenReturn(contentRetriever);
        ContentRequest contentRequest = new ContentRequest(null, contentRetrieverId, null);
        when(contentRequestParser.parse(contentRetrieverId)).thenReturn(contentRequest);
        when(contentRetriever.getChildren(contentRequest)).thenReturn(expectedList);

        List<MediaItem> result =  contentManager.getChildren(contentRetrieverId);
        assertEquals(expectedList, result);
    }

    @Test
    public void testGetChildrenNull() {
        final String incorrectId = "incorrectId";
        ContentRequest contentRequest = new ContentRequest(null, incorrectId, null);
        when(contentRequestParser.parse(incorrectId)).thenReturn(contentRequest);
        List<MediaItem> result =  contentManager.getChildren(incorrectId);
        assertNull(result);
    }

    /**
     * EXPECTED:
     * 1) SONGS_TITLE
     * 2) SONG 1
     * 3) SONG 2
     * 4) FOLDERS TITLE
     * 5) FOLDER 1
     * i.e result size 5
     */
    @Test
    public void testValidSearchQuery() {
        testSearch(LOWER_CASE_VALID_QUERY, 5);
    }

    @Test
    public void ValidSearchWithWhiteSpace() {
        final String queryWithTrailingWhitespace = "   " + VALID_QUERY + "       ";
        testSearch(queryWithTrailingWhitespace, 5);
    }

    private void testSearch(String query, int expectedResultsSize) {
        MediaItem song1 = mock(MediaItem.class);
        MediaItem song2 = mock(MediaItem.class);

        List<MediaItem> songs = new ArrayList<>();
        songs.add(song1);
        songs.add(song2);

        ContentSearcher songSearcher = getContentSearch(MediaItemType.SONGS,songs);
        MediaItem folder1 = mock(MediaItem.class);
        List<MediaItem> folders = new ArrayList<>();
        folders.add(folder1);

        ContentSearcher folderSearcher = getContentSearch(MediaItemType.FOLDER, folders);
        List<ContentSearcher> contentSearcherList = new ArrayList<>();
        contentSearcherList.add(songSearcher);
        contentSearcherList.add(folderSearcher);

        when(contentSearchers.getAll()).thenReturn(contentSearcherList);


        when(contentSearchers.get(songSearcher.getSearchCategory())).thenReturn(songSearcher);
        when(contentSearchers.get(folderSearcher.getSearchCategory())).thenReturn(folderSearcher);
        List<MediaItem> result =  contentManager.search(query);
        assertNotNull(result);
        int resultSize = result.size();
        assertEquals(expectedResultsSize, resultSize);
    }

    private ContentSearcher getContentSearch(MediaItemType type, List<MediaItem> result) {
        ContentSearcher contentSearcher = mock(ContentSearcher.class);
        when(contentSearcher.getSearchCategory()).thenReturn(type);
        when(contentSearcher.search(VALID_QUERY)).thenReturn(result);
        return contentSearcher;
    }

}