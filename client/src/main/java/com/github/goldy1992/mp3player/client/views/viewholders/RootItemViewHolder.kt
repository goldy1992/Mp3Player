package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.client.databinding.RootItemMenuBinding

class RootItemViewHolder

    constructor( context: Context, binding: RootItemMenuBinding, albumArtPainter: AlbumArtPainter?)
    : MediaItemViewHolder<RootItemMenuBinding>(context, binding, albumArtPainter) {

    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) {
        binding.title.text = MediaItemUtils.getRootTitle(item)
    }
    }