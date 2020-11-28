package com.github.goldy1992.mp3player.client.views.viewholders

import android.support.v4.media.MediaBrowserCompat
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.AlbumArtPainter

abstract class MediaItemViewHolder(itemView: View, val albumArtPainter: AlbumArtPainter?) : RecyclerView.ViewHolder(itemView) {
    abstract fun bindMediaItem(item: MediaBrowserCompat.MediaItem)

}