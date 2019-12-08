package com.github.goldy1992.mp3player.service.library.search.managers

import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.Folder
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode
import java.io.File

@LooperMode(LooperMode.Mode.PAUSED)
@RunWith(RobolectricTestRunner::class)
class FolderDatabaseManagerTest : SearchDatabaseManagerTestBase() {
    private var folderDatabaseManager: FolderDatabaseManager? = null
    @Captor
    var folderCaptor: ArgumentCaptor<Folder>? = null
    @Captor
    var deleteCaptor: ArgumentCaptor<List<String?>>? = null
    @Captor
    var insertAllCaptor: ArgumentCaptor<List<Folder?>>? = null
    @Mock
    private val folderDao: FolderDao? = null

    @Before
    override fun setup() {
        super.setup()
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(searchDatabase!!.folderDao()).thenReturn(folderDao)
        mediaItemTypeIds = MediaItemTypeIds()
        folderDatabaseManager = FolderDatabaseManager(
                contentManager!!,
                handler!!,
                mediaItemTypeIds!!,
                searchDatabase!!)
    }

    @Test
    override fun testInsert() {
        val expectedId = TEST_FILE.absolutePath
        val mediaItem = MediaItemBuilder(expectedId)
                .setDirectoryFile(TEST_FILE)
                .build()
        folderDatabaseManager!!.insert(mediaItem)
        Mockito.verify(folderDao, Mockito.times(1))!!.insert(folderCaptor!!.capture())
        val folder = folderCaptor!!.value
        Assert.assertEquals(expectedId, folder.id)
        Assert.assertEquals(EXPECTED_DIRECTORY_NAME, folder.value)
    }

    @Test
    override fun testReindex() {
        val expectedId = TEST_FILE.absolutePath
        val mediaItem = MediaItemBuilder(expectedId)
                .setDirectoryFile(TEST_FILE)
                .build()
        val toReturn = MediaItemBuilder(expectedId)
                .setDirectoryFile(TEST_FILE)
                .build()
        Mockito.`when`(contentManager!!.getChildren(mediaItemTypeIds!!.getId(MediaItemType.FOLDERS)))
                .thenReturn(listOf(toReturn))
        folderDatabaseManager!!.reindex()
        Shadows.shadowOf(handler!!.looper).idle()
        Mockito.verify(folderDao, Mockito.times(1))!!.deleteOld(deleteCaptor!!.capture())
        val idsToDelete = deleteCaptor!!.value
        Assert.assertEquals(expectedId, idsToDelete[0])
        Mockito.verify(folderDao, Mockito.times(1))!!.insertAll(insertAllCaptor!!.capture())
        val insertedFolder = insertAllCaptor!!.value[0]
        Assert.assertEquals(expectedId, insertedFolder!!.id)
        Assert.assertEquals(EXPECTED_DIRECTORY_NAME, insertedFolder.value)
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