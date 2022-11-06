package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest
import org.mockito.kotlin.mock

abstract class ContentResolverRetrieverTestBase<T : ContentResolverRetriever?> {
    var retriever: T? = null

    var contentResolver: ContentResolver = mock<ContentResolver>()

    var cursor: Cursor = mock<Cursor>()
    var handler = Handler(Looper.getMainLooper())
    var contentRequest: ContentRequest? = null
    var expectedResult: MutableList<MediaItem> = ArrayList()
    abstract fun testGetMediaType()
}