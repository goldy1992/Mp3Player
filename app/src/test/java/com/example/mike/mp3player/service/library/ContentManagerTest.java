package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.request.ContentRequest;
import com.example.mike.mp3player.service.library.content.request.ContentRequestParser;
import com.example.mike.mp3player.service.library.content.retriever.ContentRetriever;
import com.example.mike.mp3player.service.library.content.retriever.RootRetriever;
import com.example.mike.mp3player.service.library.content.searcher.ContentSearcher;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ContentManagerTest {

    private static final String VALID_QUERY = "query";

    private ContentManager contentManager;

    @Mock
    private ContentRequestParser contentRequestParser;

    @Mock
    private RootRetriever rootRetriever;

    private static Map<MediaItemType, ContentSearcher> contentSearcherMap;

    @BeforeClass
    public static void setupClass() {
        MediaItem song1 = mock(MediaItem.class);
        MediaItem song2 = mock(MediaItem.class);
        List<MediaItem> songs = new ArrayList<>();
        ContentSearcher songSearcher = getContentSearch(MediaItemType.SONG, songs);
        songs.add(song1);
        songs.add(song2);
        MediaItem folder1 = mock(MediaItem.class);
        List<MediaItem> folders = new ArrayList<>();
        folders.add(folder1);
        ContentSearcher folderSearcher = getContentSearch(MediaItemType.FOLDER, folders);

        contentSearcherMap = new HashMap<>();
        contentSearcherMap.put(songSearcher.getSearchCategory(), songSearcher);
        contentSearcherMap.put(folderSearcher.getSearchCategory(), folderSearcher);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetChildren() {
        final String id = "id";
        final List<MediaItem> expectedList = new ArrayList<>();
        final ContentRetriever contentRetriever = mock(ContentRetriever.class);
        ContentRequest contentRequest = new ContentRequest(null, id, null);
        when(contentRequestParser.parse(id)).thenReturn(contentRequest);
        when(contentRetriever.getChildren(contentRequest)).thenReturn(expectedList);

        Map<String, ContentRetriever> idToContentRetrieverMap = Collections.singletonMap(id, contentRetriever);
        this.contentManager = new ContentManager(idToContentRetrieverMap,
                null,
                contentRequestParser,
                rootRetriever);

        List<MediaItem> result =  contentManager.getChildren(id);
        assertEquals(expectedList, result);
    }

    @Test
    public void testGetChildrenNull() {
        final String incorrectId = "incorrectId";
        ContentRequest contentRequest = new ContentRequest(null, incorrectId, null);
        when(contentRequestParser.parse(incorrectId)).thenReturn(contentRequest);
        Map<String, ContentRetriever> idToContentRetrieverMap = new HashMap<>();
        this.contentManager = new ContentManager(idToContentRetrieverMap,
                null,
                contentRequestParser,
                rootRetriever);

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
        testSearch(VALID_QUERY, 5);
    }

    @Test
    public void ValidSearchWithWhiteSpace() {
        final String queryWithTrailingWhitespace = "   " + VALID_QUERY + "       ";
        testSearch(queryWithTrailingWhitespace, 5);
    }

    private void testSearch(String query, int expectedResultsSize) {
        this.contentManager = new ContentManager(null,
                contentSearcherMap,
                contentRequestParser,
                rootRetriever);

        List<MediaItem> result =  contentManager.search(query);
        assertNotNull(result);
        int resultSize = result.size();
        assertEquals(expectedResultsSize, resultSize);
    }

    private static ContentSearcher getContentSearch(MediaItemType type, List<MediaItem> result) {
        ContentSearcher contentSearcher = mock(ContentSearcher.class);
        when(contentSearcher.getSearchCategory()).thenReturn(type);
        when(contentSearcher.search(VALID_QUERY)).thenReturn(result);
        return contentSearcher;
    }

}