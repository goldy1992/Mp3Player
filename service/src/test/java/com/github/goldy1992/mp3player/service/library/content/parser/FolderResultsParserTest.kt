package com.github.goldy1992.mp3player.service.library.content.parser

import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.service.library.content.Projections.FOLDER_PROJECTION
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.io.IOException

/**
 * FolderResultsParser test
 */
@RunWith(RobolectricTestRunner::class)
class FolderResultsParserTest : ResultsParserTestBase() {

    @Rule
    @JvmField
    var tempFolder = TemporaryFolder()

    private var DIR_1: File? = null
    private var DIR_2: File? = null
    private var DIR_1_SONG_1: File? = null
    private var DIR_1_SONG_2: File? = null
    private var DIR_2_SONG_1: File? = null
    private val ID_PREFIX = "sdfgpo"
    @Before
    override fun setup() {
        try {
            DIR_1 = tempFolder.newFolder("dir1")
            DIR_2 = tempFolder.newFolder("dir2")
            DIR_1_SONG_1 = File(DIR_1, SONG_1)
            DIR_1_SONG_1!!.createNewFile()
            DIR_1_SONG_2 = File(DIR_1, SONG_2)
            DIR_1_SONG_2!!.createNewFile()
            DIR_2_SONG_1 = File(DIR_2, SONG_1)
            DIR_2_SONG_1!!.createNewFile()
            resultsParser = FolderResultsParser()
        } catch (e: IOException) {
            e.printStackTrace()
            Assert.fail()
        }
    }

    @Test
    override fun testGetType() {
        assertEquals(MediaItemType.FOLDER, resultsParser!!.type)
    }

    @Test
    fun testCreate() {
        val results = getResultsForProjection(FOLDER_PROJECTION.toTypedArray(), ID_PREFIX)
        // I.e one of the items is removed because it is in the same folder as a previous song
        val expectedResultsSize = 2
        Assert.assertEquals(expectedResultsSize.toLong(), results!!.size.toLong())
        val item1MediaId = getMediaId(results[0])
        Assert.assertNotNull(item1MediaId)
        Assert.assertTrue(item1MediaId!!.contains(DIR_1!!.name))
        // ensure songs (child sections of dir path) are removed from directory path
        Assert.assertFalse(item1MediaId.contains(SONG_1))
        val item2MediaId = getMediaId(results[1])
        Assert.assertNotNull(item2MediaId)
        Assert.assertTrue(item2MediaId!!.contains(DIR_2!!.name))
        // ensure songs (child sections of dir path) are removed from directory path
        Assert.assertFalse(item2MediaId.contains(SONG_1))
    }

    public override fun createDataSet(): Array<Array<Any?>?> {
        val dataSet: Array<Array<Any?>?> = arrayOfNulls(3)
        dataSet[0] = arrayOf(DIR_1_SONG_1!!.absolutePath)
        dataSet[1] = arrayOf(DIR_1_SONG_2!!.absolutePath)
        dataSet[2] = arrayOf(DIR_2_SONG_1!!.absolutePath)
        return dataSet
    }

    companion object {
        private const val SONG_1 = "song1"
        private const val SONG_2 = "song2"
    }
}