package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.client.databinding.NoSearchResultsItemMenuBinding

/**
 * View holder for the [R.layout.no_search_results_item_menu] when a search is performed and no
 * results are found.
 */
class NoSearchResultsViewHolder

    constructor(
        context: Context,
        binding : NoSearchResultsItemMenuBinding,
        albumArtPainter: AlbumArtPainter) : MediaItemViewHolder<NoSearchResultsItemMenuBinding>(context, binding, albumArtPainter) {

    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) {
        binding.noSearchResultsMessage.text = context.getString(R.string.no_search_results_found, item.mediaId)
    }

}