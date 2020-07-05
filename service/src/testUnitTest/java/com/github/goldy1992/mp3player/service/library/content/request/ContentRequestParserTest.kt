package com.github.goldy1992.mp3player.service.library.content.request

import com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test



class ContentRequestParserTest {
    private var contentRequestParser: ContentRequestParser? = null
    private var mediaItemTypeIds: MediaItemTypeIds? = null
    @Before
    fun setup() {
        mediaItemTypeIds = MediaItemTypeIds()
        contentRequestParser = ContentRequestParser(mediaItemTypeIds!!)
    }

    @Test
    fun testRootItem() {
        val foldersId = mediaItemTypeIds!!.getId(MediaItemType.FOLDERS)
        val folderId = mediaItemTypeIds!!.getId(MediaItemType.FOLDER)
        val contentRequest = contentRequestParser!!.parse(foldersId!!)
        assertEquals(foldersId, contentRequest!!.contentRetrieverKey)
        assertEquals(folderId, contentRequest.mediaIdPrefix) // each child of FOLDERS is of type FOLDER
        assertEquals(foldersId, contentRequest.queryString)
    }

    @Test
    fun testGetFolderSongs() {
        val folderId = mediaItemTypeIds!!.getId(MediaItemType.FOLDER)
        val path = "/a/b/mediaPath"
        val id = folderId + ID_SEPARATOR + path
        val contentRequest = contentRequestParser!!.parse(id)
        assertEquals(folderId, contentRequest!!.contentRetrieverKey)
        assertEquals(id, contentRequest.mediaIdPrefix)
        assertEquals(path, contentRequest.queryString)
    }
}