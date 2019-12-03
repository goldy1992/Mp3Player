package com.github.goldy1992.mp3player.client.views.viewholders

import android.view.View
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import org.mockito.Mock

abstract class MediaItemViewHolderTestBase<V : MediaItemViewHolder?> {
    @Mock
    var view: View? = null
    @Mock
    var albumArtPainter: AlbumArtPainter? = null
    var mediaItemViewHolder: V? = null
}