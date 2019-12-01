package com.github.goldy1992.mp3player.client.views.adapters

import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import java.util.*

abstract class MediaItemRecyclerViewAdapter(val albumArtPainter: AlbumArtPainter, val mainHandler: Handler) : RecyclerView.Adapter<MediaItemViewHolder?>() {
    var items: List<MediaBrowserCompat.MediaItem> = ArrayList()

}