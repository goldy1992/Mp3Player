package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest
import org.mockito.Mock
import java.util.*

abstract class ContentResolverRetrieverTestBase<T : ContentResolverRetriever?> {
    var retriever: T? = null
    @Mock
    var contentResolver: ContentResolver? = null
    @Mock
    var cursor: Cursor? = null
    var handler = Handler(Looper.getMainLooper())
    var contentRequest: ContentRequest? = null
    var expectedResult: List<MediaBrowserCompat.MediaItem> = ArrayList()
    abstract fun testGetMediaType()
}