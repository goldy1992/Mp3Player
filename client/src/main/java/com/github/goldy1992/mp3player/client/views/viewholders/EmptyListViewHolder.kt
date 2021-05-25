package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.databinding.EmptyItemMenuBinding

class EmptyListViewHolder

    constructor( context: Context, binding: EmptyItemMenuBinding)
    : MediaItemViewHolder<EmptyItemMenuBinding>(context, binding) {
    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) {}
}