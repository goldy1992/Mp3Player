package com.github.goldy1992.mp3player.service.library.content.observers

import android.content.ContentResolver
import android.content.ContentUris
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.data.search.managers.FolderDatabaseManager
import com.github.goldy1992.mp3player.service.library.data.search.managers.SongDatabaseManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class AudioObserverTest {
    private var audioObserver: AudioObserver? = null
    private var mediaItemTypeIds: MediaItemTypeIds? = null

    private val contentResolver: ContentResolver = mock<ContentResolver>()

    private val contentManager: ContentManager = mock<ContentManager>()

    private val songDatabaseManager: SongDatabaseManager = mock<SongDatabaseManager>()

    private val folderDatabaseManager: FolderDatabaseManager = mock<FolderDatabaseManager>()

    private val mockMediaLibrarySession: MediaLibrarySession = mock()
    private var handler: Handler? = null
    
    @Before
    fun setup() {
        handler = Handler(Looper.getMainLooper())
        mediaItemTypeIds = MediaItemTypeIds()
        audioObserver = AudioObserver(
                contentResolver,
                contentManager,
                songDatabaseManager,
                folderDatabaseManager,
                mediaItemTypeIds!!)
        audioObserver!!.init(mockMediaLibrarySession)
    }

    @Test
    fun testNullUri() {
        audioObserver!!.onChange(true)
        verify(contentManager, never()).getItem(any<Long>())
    }

    @Test
    fun testOnChangeParsableUriValidIdNoContent() {
        val expectedId = 2334L
        var uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        uri = ContentUris.withAppendedId(uri, expectedId)
        whenever(contentManager.getItem(expectedId)).thenReturn(null)
        audioObserver!!.onChange(true, uri)
        verify(contentManager, times(1)).getItem(expectedId)
        verify(songDatabaseManager, never()).insert(any<MediaItem>())
        verify(folderDatabaseManager, never()).insert(any<MediaItem>())
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
        whenever(contentManager.getItem(expectedId)).thenReturn(result)
        audioObserver!!.onChange(true, uri)
        verify(contentManager, times(1)).getItem(expectedId)
        verify(songDatabaseManager, times(1)).insert(result)
        verify(folderDatabaseManager, times(1)).insert(result)
//        verify(mockMediaLibrarySession, times(1)).notifyChildrenChanged(expectedDir.absolutePath)
//        verify(mockMediaLibrarySession, times(1)).notifyChildrenChanged(mediaItemTypeIds!!.getId(MediaItemType.FOLDERS)!!)
//        verify(mockMediaLibrarySession, times(1)).notifyChildrenChanged(mediaItemTypeIds!!.getId(MediaItemType.SONGS)!!)
    }
}