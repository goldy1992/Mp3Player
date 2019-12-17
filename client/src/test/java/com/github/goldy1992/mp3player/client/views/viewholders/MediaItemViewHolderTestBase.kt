package com.github.goldy1992.mp3player.client.views.viewholders

import android.view.View
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.nhaarman.mockitokotlin2.mock

abstract class MediaItemViewHolderTestBase<V : MediaItemViewHolder> {

    lateinit var view: View
    lateinit var albumArtPainter: AlbumArtPainter
    lateinit var mediaItemViewHolder: V

    open fun setup() {
        view = mock<View>()
        albumArtPainter = mock<AlbumArtPainter>()
    }
}