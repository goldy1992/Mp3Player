package com.github.goldy1992.mp3player.client.views.viewholders

import android.support.v4.media.MediaBrowserCompat
import android.view.View
import android.widget.TextView
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.MediaItemUtils

class RootItemViewHolder(itemView: View, albumArtPainter: AlbumArtPainter?) : MediaItemViewHolder(itemView, albumArtPainter) {
    private val title: TextView
    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) {
        title.text = MediaItemUtils.getRootTitle(item)
    }

    init {
        title = itemView.findViewById(R.id.title)
    }
}