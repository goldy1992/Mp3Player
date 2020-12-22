package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.github.goldy1992.mp3player.client.AlbumArtPainter

abstract class MediaItemViewHolder<out T : ViewBinding>
    constructor(val context : Context,
                val binding : T,
                val albumArtPainter: AlbumArtPainter?)
    : RecyclerView.ViewHolder(binding.root) {

    /**
     * Binds the [MediaBrowserCompat.MediaItem] to the [binding].
     */
    abstract fun bindMediaItem(item: MediaBrowserCompat.MediaItem)

}