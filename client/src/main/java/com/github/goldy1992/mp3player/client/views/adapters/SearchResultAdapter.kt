package com.github.goldy1992.mp3player.client.views.adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.*
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import javax.inject.Inject
import javax.inject.Named

class SearchResultAdapter @Inject constructor(albumArtPainter: AlbumArtPainter, @Named("main") mainHandler: Handler) : MediaItemRecyclerViewAdapter(albumArtPainter, mainHandler) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        var layoutInflater: LayoutInflater? = null
        return if (viewType == MediaItemType.SONG.value) { // create a new views
            layoutInflater = LayoutInflater.from(parent.context)
            val v = layoutInflater
                    .inflate(R.layout.song_item_menu, parent, false) as ViewGroup
            MySongViewHolder(v, albumArtPainter)
        } else if (viewType == MediaItemType.ROOT.value) { // create a new views
            layoutInflater = LayoutInflater.from(parent.context)
            val v = layoutInflater
                    .inflate(R.layout.root_item_menu, parent, false) as ViewGroup
            RootItemViewHolder(v, albumArtPainter)
        } else if (viewType == MediaItemType.FOLDER.value) {
            layoutInflater = LayoutInflater.from(parent.context)
            val v = layoutInflater
                    .inflate(R.layout.folder_item_menu, parent, false) as ViewGroup
            MyFolderViewHolder(v, albumArtPainter)
        } else {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_item_menu, parent, false)
            EmptyListViewHolder(view, albumArtPainter)
        }
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val item = items[holder.adapterPosition]
        holder.bindMediaItem(item)
    }

    override fun getItemViewType(position: Int): Int {
        val mediaItem = items[position]
        val mediaItemType = MediaItemUtils.getExtra(Constants.MEDIA_ITEM_TYPE, mediaItem) as MediaItemType
        return mediaItemType?.value ?: super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}