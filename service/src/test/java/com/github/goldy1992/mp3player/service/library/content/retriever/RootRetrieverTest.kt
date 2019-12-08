package com.github.goldy1992.mp3player.service.library.content.retriever

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getRootMediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class RootRetrieverTest {
    private var rootRetriever: RootRetriever? = null
    @Before
    fun setup() {
        rootRetriever = RootRetriever(MediaItemTypeIds())
    }

    @Test
    fun testGetChildren() {
        val result = rootRetriever!!.getChildren(Mockito.mock(ContentRequest::class.java))
        Assert.assertEquals(testRootItemMap!!.size.toLong(), result!!.size.toLong())
        val item1 = result[0]
        assertValidRootItem(item1)
        assertRootItemType(item1, ROOT_TYPE_1)
        val item2 = result[1]
        assertValidRootItem(item2)
        assertRootItemType(item2, ROOT_TYPE_2)
    }

    @Test
    fun testGetRootItem() {
        val result = rootRetriever!!.getRootItem(ROOT_TYPE_1)
        assertValidRootItem(result)
        assertRootItemType(result, ROOT_TYPE_1)
    }

    @Test
    fun testGetType() {
        Assert.assertEquals(MediaItemType.ROOT, rootRetriever!!.type)
    }

    private fun assertValidRootItem(item: MediaBrowserCompat.MediaItem?) {
        val mediaItemType = getMediaItemType(item)
        Assert.assertEquals(MediaItemType.ROOT, mediaItemType)
    }

    private fun assertRootItemType(item: MediaBrowserCompat.MediaItem?, expectedType: MediaItemType) {
        val mediaItemType = getRootMediaItemType(item)
        Assert.assertEquals(expectedType, mediaItemType)
    }

    companion object {
        private val ROOT_TYPE_1 = MediaItemType.SONGS
        private val ROOT_TYPE_2 = MediaItemType.FOLDERS
        private const val ROOT_TYPE_1_ID = "sfsdfsdf"
        private const val ROOT_TYPE_2_ID = ";lbmgvms"
        private var testRootItemMap: MutableMap<MediaItemType, String>? = null
        @BeforeClass
        fun setupClass() {
            testRootItemMap = HashMap()
            testRootItemMap[ROOT_TYPE_1] = ROOT_TYPE_1_ID
            testRootItemMap[ROOT_TYPE_2] = ROOT_TYPE_2_ID
        }
    }
}