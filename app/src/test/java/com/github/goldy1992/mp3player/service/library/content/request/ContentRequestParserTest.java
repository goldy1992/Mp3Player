package com.github.goldy1992.mp3player.service.library.content.request;


import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ContentRequestParserTest {

    private ContentRequestParser contentRequestParser;


    private MediaItemTypeIds mediaItemTypeIds;


    @BeforeEach
    public void setup() {
        this.mediaItemTypeIds = new MediaItemTypeIds();
        this.contentRequestParser = new ContentRequestParser(mediaItemTypeIds);
    }

    @Test
    void testRootItem() {
        final String foldersId = mediaItemTypeIds.getId(MediaItemType.FOLDERS);
        final String folderId = mediaItemTypeIds.getId(MediaItemType.FOLDER);
        ContentRequest contentRequest = contentRequestParser.parse(foldersId);
        assertEquals(foldersId, contentRequest.getContentRetrieverKey());
        assertEquals(folderId, contentRequest.getMediaIdPrefix()); // each child of FOLDERS is of type FOLDER
        assertEquals(foldersId, contentRequest.getQueryString());
    }

    @Test
    void testGetFolderSongs() {
        final String folderId = mediaItemTypeIds.getId(MediaItemType.FOLDER);
        final String path = "/a/b/mediaPath";
        String id = folderId + ID_SEPARATOR + path;
        ContentRequest contentRequest = contentRequestParser.parse(id);
        assertEquals(folderId, contentRequest.getContentRetrieverKey());
        assertEquals(id, contentRequest.getMediaIdPrefix());
        assertEquals(path, contentRequest.getQueryString());
    }
}