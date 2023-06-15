package com.github.goldy1992.mp3player.service.library.content.observers

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaLibraryService.LibraryParams
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.data.search.managers.FolderDatabaseManager
import com.github.goldy1992.mp3player.service.library.data.search.managers.SearchDatabaseManagers
import com.github.goldy1992.mp3player.service.library.data.search.managers.SongDatabaseManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AudioObserverTest {
    private lateinit var audioObserver: AudioObserver
    private val mediaItemTypeIds = MediaItemTypeIds()

    private val contentResolver: ContentResolver = mock<ContentResolver>()

    private val contentManager: ContentManager = mock<ContentManager>()

    private val songDatabaseManager: SongDatabaseManager = mock<SongDatabaseManager>()

    private val searchDatabaseManagers = mock<SearchDatabaseManagers>()

    private val folderDatabaseManager: FolderDatabaseManager = mock<FolderDatabaseManager>()

    private val mockMediaLibrarySession: MediaLibrarySession = mock()

    protected val testScheduler = TestCoroutineScheduler()
    protected val dispatcher  = UnconfinedTestDispatcher(testScheduler)
    protected val testScope = TestScope(dispatcher)

    @Before
    fun setup() {

        whenever(searchDatabaseManagers.getSongDatabaseManager()).thenReturn(songDatabaseManager)
        whenever(searchDatabaseManagers.getFolderDatabaseManager()).thenReturn(folderDatabaseManager)
        audioObserver = AudioObserver(
                contentResolver,
                contentManager,
                searchDatabaseManagers,
            dispatcher,
                mediaItemTypeIds
)
        audioObserver.init(mockMediaLibrarySession)
    }

    @Test
    fun testNullUri() = runTest {
        audioObserver.onChange(true)
        verify(contentManager, never()).getContentById(anyString())
    }

    @Test
    fun testOnChangeParsableUriValidIdNoContent() = testScope.runTest {
        val expectedId = 2334L
        var uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        uri = ContentUris.withAppendedId(uri, expectedId)
        audioObserver.onChange(true, uri)
        verify(contentManager, times(1)).getContentById(expectedId.toString())
        verify(songDatabaseManager, never()).insert(any<MediaItem>())
        verify(folderDatabaseManager, never()).insert(any<MediaItem>())
    }

    @Test
    fun testOnChangeParsableUriWithValidId() = runTest {
        val expectedId = 2334L
        val expectedDir = File("/a/b/c")
        var uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        uri = ContentUris.withAppendedId(uri, expectedId)
        val result = MediaItemBuilder("sf")
                .setDirectoryFile(expectedDir)
                .build()
        whenever(contentManager.getContentById(expectedId.toString())).thenReturn(result)
        audioObserver.onChange(true, uri)
        verify(contentManager, times(1)).getContentById(expectedId.toString())
        verify(songDatabaseManager, times(1)).insert(result)
        verify(folderDatabaseManager, times(1)).insert(result)
        argumentCaptor<String> {
            val expectedSongsId = mediaItemTypeIds.getId(MediaItemType.SONGS)
            val expectedFoldersId = mediaItemTypeIds.getId(MediaItemType.FOLDERS)
            verify(mockMediaLibrarySession, times(3)).notifyChildrenChanged(this.capture(), eq(1), any())
            val actualFolderId = this.firstValue
            assertEquals(expectedDir.absolutePath, actualFolderId)
            val actualSongId = this.secondValue
            assertEquals(expectedSongsId, actualSongId)
            val actualFoldersId = this.thirdValue
            assertEquals(expectedFoldersId, actualFoldersId)


        }
//        verify(mockMediaLibrarySession, times(1)).notifyChildrenChanged(expectedDir.absolutePath, 1, any())
//        verify(mockMediaLibrarySession, times(1)).notifyChildrenChanged(mediaItemTypeIds.getId(MediaItemType.FOLDERS), 1, any())
//        verify(mockMediaLibrarySession, times(1)).notifyChildrenChanged(mediaItemTypeIds.getId(MediaItemType.SONGS), 1, any())
    }
}