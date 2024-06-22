package com.github.goldy1992.mp3player.service.library.content.retriever

import android.os.Looper
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.content.retrievers.FoldersRetriever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class FoldersRetrieverTest : ContentResolverRetrieverTestBase<FoldersRetriever?>() {

    var resultsParser: FolderResultsParser = mock<FolderResultsParser>()

    @Before
    fun setup() {
        retriever = spy(FoldersRetriever(contentResolver, resultsParser))
    }

    @Test
    fun testGetChildren() {
        val directoryPath = "/a/b/c"
        val directoryName = "c"
        // use mock file to avoid different OS path styles
        val file = mock<File>()
        whenever(file.absolutePath).thenReturn(directoryPath)
        whenever(file.name).thenReturn(directoryName)
        val mediaItem = MediaItemBuilder(
            mediaId = "id",
            file = file
        ).build()
        expectedResult.add(mediaItem)
        whenever(contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, retriever!!.projection, null, null, null)).thenReturn(cursor)
        whenever(resultsParser.create(cursor)).thenReturn(expectedResult)
        /* IN ORDER for the database update code to be hit, there needs to be difference in file
        numbers to call it. This is a flaw in the design and will be addressed in another issue */
        val result = retriever!!.getChildren(mediaItem.mediaId)
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