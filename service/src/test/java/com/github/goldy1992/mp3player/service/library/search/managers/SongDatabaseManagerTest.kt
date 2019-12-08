package com.github.goldy1992.mp3player.service.library.search.managers

import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.Song
import com.github.goldy1992.mp3player.service.library.search.SongDao
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

@LooperMode(LooperMode.Mode.PAUSED)
@RunWith(RobolectricTestRunner::class)
class SongDatabaseManagerTest : SearchDatabaseManagerTestBase() {
    private var songDatabaseManager: SongDatabaseManager? = null
    @Captor
    var songCaptor: ArgumentCaptor<Song>? = null
    @Captor
    var deleteCaptor: ArgumentCaptor<List<String?>>? = null
    @Captor
    var insertAllCaptor: ArgumentCaptor<List<Song?>>? = null
    @Mock
    private val songDao: SongDao? = null

    @Before
    override fun setup() {
        super.setup()
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(searchDatabase!!.songDao()).thenReturn(songDao)
        mediaItemTypeIds = MediaItemTypeIds()
        songDatabaseManager = SongDatabaseManager(
                contentManager!!,
                handler!!,
                mediaItemTypeIds!!,
                searchDatabase!!)
    }

    @Test
    override fun testInsert() {
        val expectedId = "23fsdf"
        val songTitle = "songTitle"
        val expectedValue = songTitle.toUpperCase()
        val mediaItem = MediaItemBuilder(expectedId)
                .setTitle(expectedValue)
                .build()
        songDatabaseManager!!.insert(mediaItem)
        Mockito.verify(songDao, Mockito.times(1))!!.insert(songCaptor!!.capture())
        val song = songCaptor!!.value
        Assert.assertEquals(expectedId, song.id)
        Assert.assertEquals(expectedValue, song.value)
    }

    @Test
    override fun testReindex() {
        val expectedId = "sdkjdsf"
        val title = "expectedTitle"
        val expectedTitle = title.toUpperCase()
        val toReturn = MediaItemBuilder(expectedId)
                .setTitle(title)
                .build()
        Mockito.`when`(contentManager!!.getChildren(mediaItemTypeIds!!.getId(MediaItemType.SONGS)))
                .thenReturn(listOf(toReturn))
        songDatabaseManager!!.reindex()
        Shadows.shadowOf(handler!!.looper).idle()
        Mockito.verify(songDao, Mockito.times(1))!!.deleteOld(deleteCaptor!!.capture())
        val idsToDelete = deleteCaptor!!.value
        Assert.assertEquals(expectedId, idsToDelete[0])
        Mockito.verify(songDao, Mockito.times(1))!!.insertAll(insertAllCaptor!!.capture())
        val insertedFolder = insertAllCaptor!!.value[0]
        Assert.assertEquals(expectedId, insertedFolder!!.id)
        Assert.assertEquals(expectedTitle, insertedFolder.value)
    }
}