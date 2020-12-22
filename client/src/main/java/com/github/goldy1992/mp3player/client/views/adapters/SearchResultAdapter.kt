package com.github.goldy1992.mp3player.client.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MyFolderViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MySongViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.NoSearchResultsViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.RootItemViewHolder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.databinding.FolderItemMenuBinding
import com.github.goldy1992.mp3player.databinding.NoSearchResultsItemMenuBinding
import com.github.goldy1992.mp3player.databinding.RootItemMenuBinding
import com.github.goldy1992.mp3player.databinding.SongItemMenuBinding
import javax.inject.Inject

class SearchResultAdapter

    @Inject
    constructor(albumArtPainter: AlbumArtPainter) : MediaItemRecyclerViewAdapter(albumArtPainter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder<ViewBinding> {
        Log.e(logTag(), "creating new view holder")
        val context : Context = parent.context
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        return when (viewType) {
            MediaItemType.SONG.value -> { // create a new views
                val view = SongItemMenuBinding.inflate(layoutInflater, parent, false)
                MySongViewHolder(context, view, albumArtPainter)
            }
            MediaItemType.ROOT.value -> { // create a new views
                val view = RootItemMenuBinding.inflate(layoutInflater, parent, false)
                RootItemViewHolder(context, view, albumArtPainter)
            }
            MediaItemType.FOLDER.value -> {
                val view = FolderItemMenuBinding.inflate(layoutInflater, parent, false)
                MyFolderViewHolder(context, view, albumArtPainter)
            }
            else -> {
                createEmptyViewHolder(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder<ViewBinding>, position: Int) {
        val item = getItem(position)
        Log.e(logTag(), "on bind view holder, item: " + item.mediaId)
        holder.bindMediaItem(item)
    }

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