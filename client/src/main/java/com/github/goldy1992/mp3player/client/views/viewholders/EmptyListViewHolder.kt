package com.github.goldy1992.mp3player.client.views.viewholders

import android.support.v4.media.MediaBrowserCompat
import android.view.View
import com.github.goldy1992.mp3player.client.AlbumArtPainter

class EmptyListViewHolder(itemView: View, albumArtPainter: AlbumArtPainter?) : MediaItemViewHolder(itemView, albumArtPainter) {
    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) {}
}