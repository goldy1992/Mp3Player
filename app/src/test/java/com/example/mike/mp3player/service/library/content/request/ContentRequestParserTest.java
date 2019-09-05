package com.example.mike.mp3player.service.library.content.request;


import com.example.mike.mp3player.commons.MediaItemType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.example.mike.mp3player.commons.Constants.ID_SEPARATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ContentRequestParserTest {

    private static ContentRequestParser contentRequestParser;

    private static final String SONGS_ID = "SONGS_ID";
    private static final String FOLDERS_ID = "FOLDERS_ID";
    private static final String FOLDER_ID = "FOLDER_ID";
    private static Map<String, MediaItemType> idToMediaItemMap;
    private static Map<MediaItemType, String> mediaItemToIdMap;
    @BeforeAll
    public static void setupClass() {
        BiMap<String, MediaItemType> biMap = HashBiMap.create();
        biMap.put(SONGS_ID, MediaItemType.SONGS);
        biMap.put(FOLDERS_ID, MediaItemType.FOLDERS);
        biMap.put(FOLDER_ID, MediaItemType.FOLDER);
        idToMediaItemMap = biMap;
        mediaItemToIdMap = biMap.inverse();
    }

    @BeforeEach
    public void setup() {
        this.contentRequestParser = new ContentRequestParser(mediaItemToIdMap, idToMediaItemMap);
    }

    @Test
    void testRootItem() {
        ContentRequest contentRequest = contentRequestParser.parse(FOLDERS_ID);
        assertEquals(FOLDERS_ID, contentRequest.getContentRetrieverKey());
        assertEquals(FOLDER_ID, contentRequest.getMediaIdPrefix()); // each child of FOLDERS is of type FOLDER
        assertEquals(FOLDERS_ID, contentRequest.getQueryString());
    }

    @Test
    void testGetFolderSongs() {
        final String path = "/a/b/mediaPath";
        String id = FOLDER_ID + ID_SEPARATOR + path;
        ContentRequest contentRequest = contentRequestParser.parse(id);
        assertEquals(FOLDER_ID, contentRequest.getContentRetrieverKey());
        assertEquals(id, contentRequest.getMediaIdPrefix());
        assertEquals(path, contentRequest.getQueryString());
    }
}