package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.databinding.EmptyItemMenuBinding

class EmptyListViewHolder

    constructor( context: Context, binding: EmptyItemMenuBinding, albumArtPainter: AlbumArtPainter?)
    : MediaItemViewHolder<EmptyItemMenuBinding>(context, binding, albumArtPainter) {
    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) {}
}