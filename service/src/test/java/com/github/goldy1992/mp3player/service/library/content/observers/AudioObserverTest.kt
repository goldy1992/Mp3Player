package com.github.goldy1992.mp3player.service.library.content.observers

import android.content.ContentResolver
import android.content.ContentUris
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.managers.FolderDatabaseManager
import com.github.goldy1992.mp3player.service.library.search.managers.SongDatabaseManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class AudioObserverTest {
    private var audioObserver: AudioObserver? = null
    private var mediaItemTypeIds: MediaItemTypeIds? = null
    @Mock
    private val contentResolver: ContentResolver? = null
    @Mock
    private val contentManager: ContentManager? = null
    @Mock
    private val songDatabaseManager: SongDatabaseManager? = null
    @Mock
    private val folderDatabaseManager: FolderDatabaseManager? = null
    @Mock
    private val mediaPlaybackService: MediaPlaybackService? = null
    private var handler: Handler? = null
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        handler = Handler(Looper.getMainLooper())
        mediaItemTypeIds = MediaItemTypeIds()
        audioObserver = AudioObserver(
                handler,
                contentResolver!!,
                contentManager!!,
                songDatabaseManager!!,
                folderDatabaseManager!!,
                mediaItemTypeIds!!)
        audioObserver!!.init(mediaPlaybackService)
    }

    @Test
    fun testNullUri() {
        audioObserver!!.onChange(true)
        Mockito.verify(contentManager, Mockito.never())!!.getItem(ArgumentMatchers.anyLong())
    }

    @Test
    fun testOnChangeParsableUriValidIdNoContent() {
        val expectedId = 2334L
        var uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        uri = ContentUris.withAppendedId(uri, expectedId)
        Mockito.`when`(contentManager!!.getItem(expectedId)).thenReturn(null)
        audioObserver!!.onChange(true, uri)
        Mockito.verify(contentManager, Mockito.times(1))!!.getItem(expectedId)
        Mockito.verify(songDatabaseManager, Mockito.never())!!.insert(ArgumentMatchers.any(MediaBrowserCompat.MediaItem::class.java))
        Mockito.verify(folderDatabaseManager, Mockito.never())!!.insert(ArgumentMatchers.any(MediaBrowserCompat.MediaItem::class.java))
    }

    @Test
    fun testOnChangeParsableUriWithValidId() {
        val expectedId = 2334L
        val expectedDir = File("/a/b/c")
        var uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        uri = ContentUris.withAppendedId(uri, expectedId)
        val result = MediaItemBuilder("sf")
                .setDirectoryFile(expectedDir)
                .build()
        Mockito.`when`(contentManager!!.getItem(expectedId)).thenReturn(result)
        audioObserver!!.onChange(true, uri)
        Mockito.verify(contentManager, Mockito.times(1))!!.getItem(expectedId)
        Mockito.verify(songDatabaseManager, Mockito.times(1))!!.insert(result)
        Mockito.verify(folderDatabaseManager, Mockito.times(1))!!.insert(result)
        Mockito.verify(mediaPlaybackService, Mockito.times(1))!!.notifyChildrenChanged(expectedDir.absolutePath)
        Mockito.verify(mediaPlaybackService, Mockito.times(1))!!.notifyChildrenChanged(mediaItemTypeIds!!.getId(MediaItemType.FOLDERS)!!)
        Mockito.verify(mediaPlaybackService, Mockito.times(1))!!.notifyChildrenChanged(mediaItemTypeIds!!.getId(MediaItemType.SONGS)!!)
    }
}