package com.github.goldy1992.mp3player.service.library.search.managers

import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.data.search.Song
import com.github.goldy1992.mp3player.service.library.data.search.SongDao
import com.github.goldy1992.mp3player.service.library.data.search.managers.SongDatabaseManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@LooperMode(LooperMode.Mode.PAUSED)
@RunWith(RobolectricTestRunner::class)
class SongDatabaseManagerTest : SearchDatabaseManagerTestBase() {
    private lateinit var songDatabaseManager: SongDatabaseManager

    private val songDao: SongDao = mock<SongDao>()

    @Before
    override fun setup() {
        super.setup()
        whenever(searchDatabase.songDao()).thenReturn(songDao)
        mediaItemTypeIds = MediaItemTypeIds()
        songDatabaseManager = SongDatabaseManager(
                contentManager,
                mediaItemTypeIds,
                searchDatabase)
    }

    @Test
    override fun testInsert() {
        val expectedId = "23fsdf"
        val songTitle = "songTitle"
        val expectedValue = songTitle.uppercase(Locale.ROOT)
        val mediaItem = MediaItemBuilder(expectedId)
                .setTitle(expectedValue)
                .build()
        argumentCaptor<Song>().apply {
            songDatabaseManager.insert(mediaItem)
            verify(songDao, times(1)).insert(capture())
            val song = firstValue
            Assert.assertEquals(expectedId, song.id)
            Assert.assertEquals(expectedValue, song.value)
        }
    }

    @Test
    fun testReindexCheckDeleteOld() = testScope.runTest {
        val expectedId = "sdkjdsf"
        val title = "expectedTitle"
        val toReturn = MediaItemBuilder(expectedId)
                .setTitle(title)
                .build()
        whenever(contentManager.getChildren(mediaItemTypeIds.getId(MediaItemType.SONGS)))
                .thenReturn(listOf(toReturn))

        argumentCaptor<List<String>>().apply {
            songDatabaseManager.reindex()
            verify(songDao, times(1)).deleteOld(capture())
            val idsToDelete = firstValue[0]
            Assert.assertEquals(expectedId, idsToDelete)
        }
    }

    @Test
    fun testReindexCheckInsertAll() = testScope.runTest {
        val expectedId = "sdkjdsf"
        val title = "expectedTitle"
        val expectedTitle = title.uppercase(Locale.ROOT)
        val toReturn = MediaItemBuilder(expectedId)
                .setTitle(title)
                .build()
        whenever(contentManager.getChildren(mediaItemTypeIds.getId(MediaItemType.SONGS)))
                .thenReturn(listOf(toReturn))
        argumentCaptor<List<Song>>().apply {
        songDatabaseManager.reindex()
        verify(songDao, times(1)).insertAll(capture())
        val insertedFolder = firstValue[0]
        Assert.assertEquals(expectedId, insertedFolder.id)
        Assert.assertEquals(expectedTitle, insertedFolder.value)
        }
    }
}