package com.github.goldy1992.mp3player.service.library.content.retriever

import android.os.Looper
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest
import com.github.goldy1992.mp3player.service.library.search.Folder
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

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class FoldersRetrieverTest : ContentResolverRetrieverTestBase<FoldersRetriever?>() {
    @Mock
    var resultsParser: FolderResultsParser? = null
    @Captor
    var captor: ArgumentCaptor<List<Folder>>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        retriever = Mockito.spy(FoldersRetriever(contentResolver!!, resultsParser!!, handler))
    }

    @Test
    fun testGetChildren() {
        contentRequest = ContentRequest("x", "y", "z")
        val directoryPath = "/a/b/c"
        val directoryName = "c"
        // use mock file to avoid different OS path styles
        val file = Mockito.mock(File::class.java)
        Mockito.`when`(file.absolutePath).thenReturn(directoryPath)
        Mockito.`when`(file.name).thenReturn(directoryName)
        val mediaItem = MediaItemBuilder("id")
                .setDirectoryFile(file)
                .build()
        expectedResult.add(mediaItem)
        Mockito.`when`(contentResolver!!.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, retriever!!.projection, null, null, null)).thenReturn(cursor)
        Mockito.`when`(resultsParser!!.create(cursor!!, contentRequest!!.mediaIdPrefix!!)).thenReturn(expectedResult)
        /* IN ORDER for the database update code to be hit, there needs to be difference in file
        numbers to call it. This is a flaw in the design and will be addressed in another issue */
        val result = retriever!!.getChildren(contentRequest!!)
        // call remaining looper messages
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        // assert results are the expected ones
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    override fun testGetMediaType() {
        Assert.assertEquals(MediaItemType.FOLDER, retriever!!.type)
    }
}