package com.github.goldy1992.mp3player.client.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.views.viewholders.*
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import javax.inject.Inject

class SearchResultAdapter

    @Inject
    constructor(albumArtPainter: AlbumArtPainter) : MediaItemRecyclerViewAdapter(albumArtPainter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val layoutInflater: LayoutInflater?
        return when (viewType) {
            MediaItemType.SONG.value -> { // create a new views
                layoutInflater = LayoutInflater.from(parent.context)
                val v = layoutInflater
                        .inflate(R.layout.song_item_menu, parent, false) as ViewGroup
                MySongViewHolder(v, albumArtPainter)
            }
            MediaItemType.ROOT.value -> { // create a new views
                layoutInflater = LayoutInflater.from(parent.context)
                val v = layoutInflater
                        .inflate(R.layout.root_item_menu, parent, false) as ViewGroup
                RootItemViewHolder(v, albumArtPainter)
            }
            MediaItemType.FOLDER.value -> {
                layoutInflater = LayoutInflater.from(parent.context)
                val v = layoutInflater
                        .inflate(R.layout.folder_item_menu, parent, false) as ViewGroup
                MyFolderViewHolder(v, albumArtPainter)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.empty_item_menu, parent, false)
                EmptyListViewHolder(view, albumArtPainter)
            }
        }
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val item = items[holder.adapterPosition]
        holder.bindMediaItem(item)
    }

    override fun getItemViewType(position: Int): Int {
        val mediaItem = items[position]
        val mediaItemType : MediaItemType? = MediaItemUtils.getMediaItemType(mediaItem)
        return mediaItemType?.value ?: super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}