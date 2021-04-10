package com.github.goldy1992.mp3player.service.library.search.managers

import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.Folder
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode
import java.io.File

@LooperMode(LooperMode.Mode.PAUSED)
@RunWith(RobolectricTestRunner::class)
class FolderDatabaseManagerTest : SearchDatabaseManagerTestBase() {

    private lateinit var folderDatabaseManager: FolderDatabaseManager

    private var folderDao: FolderDao = mock<FolderDao>()

    @Before
    override fun setup() {
        super.setup()
        whenever(searchDatabase.folderDao()).thenReturn(folderDao)
        mediaItemTypeIds = MediaItemTypeIds()
        folderDatabaseManager = FolderDatabaseManager(
                contentManager,
                mediaItemTypeIds,
                searchDatabase)
    }

    @Test
    override fun testInsert() {
        val expectedId = TEST_FILE.absolutePath
        val mediaItem = MediaItemBuilder(expectedId)
                .setDirectoryFile(TEST_FILE)
                .build()
        argumentCaptor<Folder>().apply {
            folderDatabaseManager.insert(mediaItem)
            verify(folderDao, times(1)).insert(capture())
            val folder = firstValue
            Assert.assertEquals(expectedId, folder.id)
            Assert.assertEquals(EXPECTED_DIRECTORY_NAME, folder.value)
        }
    }

    @Test
    fun testReindexCheckDeleteOld() {
        val expectedId = TEST_FILE.absolutePath

        val toReturn = MediaItemBuilder(expectedId)
                .setDirectoryFile(TEST_FILE)
                .build()
        whenever(contentManager.getChildren(mediaItemTypeIds.getId(MediaItemType.FOLDERS)))
                .thenReturn(listOf(toReturn))

        argumentCaptor<List<String>>().apply {
            folderDatabaseManager.reindex()
            Shadows.shadowOf(handler.looper).idle()
            verify(folderDao, times(1)).deleteOld(capture())
            val idsToDelete = firstValue
            Assert.assertEquals(expectedId, idsToDelete[0])
        }
    }

    @Test
    fun testReindexCheckInsertAll() {
        val expectedId = TEST_FILE.absolutePath
        val toReturn = MediaItemBuilder(expectedId)
                .setDirectoryFile(TEST_FILE)
                .build()
        whenever(contentManager.getChildren(mediaItemTypeIds.getId(MediaItemType.FOLDERS)))
                .thenReturn(listOf(toReturn))

        argumentCaptor<List<Folder>>().apply {
                folderDatabaseManager.reindex()
                Shadows.shadowOf(handler.looper).idle()
                     verify(folderDao, times(1)).insertAll(capture())
                val insertedFolder = firstValue[0]
                Assert.assertEquals(expectedId, insertedFolder.id)
                Assert.assertEquals(EXPECTED_DIRECTORY_NAME, insertedFolder.value)

        }

    }

    companion object {
        private const val TEST_DIRECTORY_NAME = "fileName"
        private val EXPECTED_DIRECTORY_NAME = TEST_DIRECTORY_NAME.toUpperCase()
        private val TEST_DIRECTORY_PATH = (File.separator
                + "a" + File.separator
                + "b" + File.separator
                + TEST_DIRECTORY_NAME)
        private val TEST_FILE = File(TEST_DIRECTORY_PATH)
    }
}