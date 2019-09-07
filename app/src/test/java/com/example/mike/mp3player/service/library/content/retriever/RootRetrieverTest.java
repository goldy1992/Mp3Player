package com.example.mike.mp3player.service.library.content.retriever;


import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.library.content.request.ContentRequest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class RootRetrieverTest {
    private RootRetriever rootRetriever;
    private static final MediaItemType ROOT_TYPE_1 = MediaItemType.SONGS;
    private static final MediaItemType ROOT_TYPE_2 = MediaItemType.FOLDERS;
    private static final String ROOT_TYPE_1_ID = "sfsdfsdf";
    private static final String ROOT_TYPE_2_ID = ";lbmgvms";
    private static Map<MediaItemType, String> testRootItemMap;

    @BeforeClass
    public static void setupClass() {
        testRootItemMap = new HashMap<>();
        testRootItemMap.put(ROOT_TYPE_1, ROOT_TYPE_1_ID);
        testRootItemMap.put(ROOT_TYPE_2, ROOT_TYPE_2_ID);
    }

    @Before
    public void setup() {
        this.rootRetriever = new RootRetriever(testRootItemMap);
    }

    @Test
    public void testGetChildren() {
        List<MediaItem> result = this.rootRetriever.getChildren(mock(ContentRequest.class));
        assertEquals(testRootItemMap.size(), result.size());
        MediaItem item1 = result.get(0);
        assertValidRootItem(item1);
        assertRootItemType(item1, ROOT_TYPE_1);

        MediaItem item2 = result.get(1);
        assertValidRootItem(item2);
        assertRootItemType(item2, ROOT_TYPE_2);
    }

    @Test
    public void testGetRootItem() {
        MediaItem result = rootRetriever.getRootItem(ROOT_TYPE_1);
        assertValidRootItem(result);
        assertRootItemType(result, ROOT_TYPE_1);
    }

    @Test
    public void testGetType() {
        assertEquals(MediaItemType.ROOT, rootRetriever.getType());
    }

    private void assertValidRootItem(MediaItem item) {
        MediaItemType mediaItemType = MediaItemUtils.getMediaItemType(item);
        assertEquals(MediaItemType.ROOT, mediaItemType);
    }

    private void assertRootItemType(MediaItem item, MediaItemType expectedType) {
        MediaItemType mediaItemType = MediaItemUtils.getRootMediaItemType(item);
        assertEquals(expectedType, mediaItemType);
    }

}