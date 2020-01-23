package com.github.goldy1992.mp3player.client.views.adapters

import android.os.Handler
import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder

abstract class MediaItemRecyclerViewAdapter
    constructor (val albumArtPainter: AlbumArtPainter)
    : RecyclerView.Adapter<MediaItemViewHolder?>() {

    var items: MutableList<MediaItem> = arrayListOf()

}