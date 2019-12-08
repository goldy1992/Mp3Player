package com.github.goldy1992.mp3player.service.library.content.request

import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ContentRequestParserTest {
    private var contentRequestParser: ContentRequestParser? = null
    private var mediaItemTypeIds: MediaItemTypeIds? = null
    @BeforeEach
    fun setup() {
        mediaItemTypeIds = MediaItemTypeIds()
        contentRequestParser = ContentRequestParser(mediaItemTypeIds!!)
    }

    @Test
    fun testRootItem() {
        val foldersId = mediaItemTypeIds!!.getId(MediaItemType.FOLDERS)
        val folderId = mediaItemTypeIds!!.getId(MediaItemType.FOLDER)
        val contentRequest = contentRequestParser!!.parse(foldersId!!)
        Assertions.assertEquals(foldersId, contentRequest!!.contentRetrieverKey)
        Assertions.assertEquals(folderId, contentRequest.mediaIdPrefix) // each child of FOLDERS is of type FOLDER
        Assertions.assertEquals(foldersId, contentRequest.queryString)
    }

    @Test
    fun testGetFolderSongs() {
        val folderId = mediaItemTypeIds!!.getId(MediaItemType.FOLDER)
        val path = "/a/b/mediaPath"
        val id = folderId + ID_SEPARATOR + path
        val contentRequest = contentRequestParser!!.parse(id)
        Assertions.assertEquals(folderId, contentRequest!!.contentRetrieverKey)
        Assertions.assertEquals(id, contentRequest.mediaIdPrefix)
        Assertions.assertEquals(path, contentRequest.queryString)
    }
}