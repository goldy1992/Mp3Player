package com.github.goldy1992.mp3player.service.library.search.managers

import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.ContentManagerResult
import com.github.goldy1992.mp3player.service.library.data.search.Folder
import com.github.goldy1992.mp3player.service.library.data.search.FolderDao
import com.github.goldy1992.mp3player.service.library.data.search.managers.FolderDatabaseManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class FolderDatabaseManagerTest : SearchDatabaseManagerTestBase() {

    private lateinit var folderDatabaseManager: FolderDatabaseManager

    private var folderDao: FolderDao = mock<FolderDao>()

    private val testRootItemId = "gsdfskf"
    private val testDirectoryName = "fileName"
    private val expectedDirectoryName = testDirectoryName.uppercase()
    private val testDirectoryPath = (File.separator
            + "a" + File.separator
            + "b" + File.separator
            + testDirectoryName)
    private val testFile = File(testDirectoryPath)

    @Before
    override fun setup() {
        super.setup()
        whenever(searchDatabase.folderDao()).thenReturn(folderDao)
        mediaItemTypeIds = MediaItemTypeIds()
        folderDatabaseManager = FolderDatabaseManager(
            contentManager,
            testRootItemId,
            searchDatabase
        )
    }

    @Test
    override fun testInsert() {
        val expectedId = testFile.absolutePath
        val mediaItem = MediaItemBuilder(expectedId)
                .setDirectoryFile(testFile)
                .build()
        argumentCaptor<Folder>().apply {
            folderDatabaseManager.insert(mediaItem)
            verify(folderDao, times(1)).insert(capture())
            val folder = firstValue
            Assert.assertEquals(expectedId, folder.id)
            Assert.assertEquals(expectedDirectoryName, folder.value)
        }
    }

    @Test
    fun testReindexCheckDeleteOld() = testScope.runTest {
        val expectedId = testFile.absolutePath

        val toReturn = MediaItemBuilder(expectedId)
                .setDirectoryFile(testFile)
                .build()
        val cmr = ContentManagerResult(listOf(toReturn), 1, true)
        whenever(contentManager.getChildren(testRootItemId))
                .thenReturn(cmr)

        argumentCaptor<List<String>>().apply {
            folderDatabaseManager.reindex()
            verify(folderDao, times(1)).deleteOld(capture())
            val idsToDelete = firstValue
            Assert.assertEquals(expectedId, idsToDelete[0])
        }
    }

    @Test
    fun testReindexCheckInsertAll() = testScope.runTest {
        val expectedId = testFile.absolutePath
        val toReturn = MediaItemBuilder(expectedId)
                .setDirectoryFile(testFile)
                .build()
        val cmr = ContentManagerResult(listOf(toReturn), 1, true)
        whenever(contentManager.getChildren(testRootItemId))
                .thenReturn(cmr)


        argumentCaptor<List<Folder>>().apply {
            folderDatabaseManager.reindex()
                     verify(folderDao, times(1)).insertAll(capture())
                val insertedFolder = firstValue[0]
                Assert.assertEquals(expectedId, insertedFolder.id)
                Assert.assertEquals(expectedDirectoryName, insertedFolder.value)

        }

    }




}