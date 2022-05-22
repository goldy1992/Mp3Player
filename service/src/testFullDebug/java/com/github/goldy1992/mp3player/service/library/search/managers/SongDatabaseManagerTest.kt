package com.github.goldy1992.mp3player.service.library.search.managers

import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.Song
import com.github.goldy1992.mp3player.service.library.search.SongDao
import org.mockito.kotlin.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode

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
        val expectedValue = songTitle.toUpperCase()
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
    fun testReindexCheckDeleteOld() {
        val expectedId = "sdkjdsf"
        val title = "expectedTitle"
        val toReturn = MediaItemBuilder(expectedId)
                .setTitle(title)
                .build()
        whenever(contentManager.getChildren(mediaItemTypeIds.getId(MediaItemType.SONGS)))
                .thenReturn(listOf(toReturn))

        argumentCaptor<List<String>>().apply {
            songDatabaseManager.reindex()
            Shadows.shadowOf(handler.looper).idle()
            verify(songDao, times(1)).deleteOld(capture())
            val idsToDelete = firstValue[0]
            Assert.assertEquals(expectedId, idsToDelete)
        }
    }

    @Test
    fun testReindexCheckInsertAll() {
        val expectedId = "sdkjdsf"
        val title = "expectedTitle"
        val expectedTitle = title.toUpperCase()
        val toReturn = MediaItemBuilder(expectedId)
                .setTitle(title)
                .build()
        whenever(contentManager.getChildren(mediaItemTypeIds.getId(MediaItemType.SONGS)))
                .thenReturn(listOf(toReturn))
        argumentCaptor<List<Song>>().apply {
        songDatabaseManager.reindex()
        Shadows.shadowOf(handler.looper).idle()
        verify(songDao, times(1)).insertAll(capture())
        val insertedFolder = firstValue[0]
        Assert.assertEquals(expectedId, insertedFolder.id)
        Assert.assertEquals(expectedTitle, insertedFolder.value)
        }
    }
}