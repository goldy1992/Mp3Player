package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.ListPreloader.PreloadModelProvider
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.views.viewholders.EmptyListViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.l4digital.fastscroll.FastScroller

abstract class MyGenericRecyclerViewAdapter
    (albumArtPainter: AlbumArtPainter) : MediaItemRecyclerViewAdapter(albumArtPainter),
        FastScroller.SectionIndexer,
        PreloadModelProvider<MediaItem?>,
        LogTagger {

    private val EMPTY_MEDIA_ID = "EMPTY_MEDIA_ID"

    val EMPTY_VIEW_TYPE = -1
    private val EMPTY_LIST_ITEM = buildEmptyListMediaItem()


//    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaItem>) {
//        if (!children.isEmpty()) {
//            this.items = children
//            notifyDataSetChanged()
//        } else {
//            addNoChildrenFoundItem()
//        }
//    }


    fun createEmptyViewHolder(parent: ViewGroup): MediaItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_item_menu, parent, false)
            return EmptyListViewHolder(view, albumArtPainter)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == Constants.FIRST && isEmptyRecycleView) {
            EMPTY_VIEW_TYPE
        } else super.getItemViewType(position)
    }

//    private fun addNoChildrenFoundItem() {
//        items.add(EMPTY_LIST_ITEM)
//        notifyDataSetChanged()
//    }

    private fun buildEmptyListMediaItem(): MediaItem {
        val mediaDescriptionCompat = MediaDescriptionCompat.Builder()
                .setMediaId(EMPTY_MEDIA_ID)
                .build()
        return MediaItem(mediaDescriptionCompat, MediaItem.FLAG_PLAYABLE)
    }

    protected val isEmptyRecycleView: Boolean
        get() = !hasItems() || getItem(Constants.FIRST) == EMPTY_LIST_ITEM


    fun hasItems() : Boolean {
        return itemCount > 0
    }
}