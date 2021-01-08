package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.ListPreloader.PreloadModelProvider
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.EmptyListViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.databinding.EmptyItemMenuBinding
import com.l4digital.fastscroll.FastScroller

abstract class MediaItemListFastScrollListAdapter
    (albumArtPainter: AlbumArtPainter) : MediaItemListRecyclerViewAdapter(albumArtPainter),
        FastScroller.SectionIndexer,
        PreloadModelProvider<MediaItem?> {


    override fun createEmptyViewHolder(parent: ViewGroup): MediaItemViewHolder<ViewBinding> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = EmptyItemMenuBinding.inflate(layoutInflater, parent, false)
            return EmptyListViewHolder(parent.context, view, albumArtPainter)
    }

}