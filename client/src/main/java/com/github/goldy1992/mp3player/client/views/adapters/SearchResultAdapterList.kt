package com.github.goldy1992.mp3player.client.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.NoSearchResultsViewHolder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.client.databinding.NoSearchResultsItemMenuBinding
import javax.inject.Inject

class SearchResultAdapterList

    @Inject
    constructor(albumArtPainter: AlbumArtPainter) : MediaItemListRecyclerViewAdapter(albumArtPainter) {

    override fun getItemViewType(position: Int): Int {
        val mediaItem = getItem(position)
        val mediaItemType : MediaItemType? = MediaItemUtils.getMediaItemType(mediaItem)
        return mediaItemType?.value ?: super.getItemViewType(position)
    }

    override fun createEmptyViewHolder(parent: ViewGroup): NoSearchResultsViewHolder {
        val view = NoSearchResultsItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoSearchResultsViewHolder(parent.context, view, albumArtPainter)
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "SEARCH_RESULT_ADAPTER"
    }
}