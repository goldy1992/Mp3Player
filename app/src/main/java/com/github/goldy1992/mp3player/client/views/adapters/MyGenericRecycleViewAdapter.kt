package com.github.goldy1992.mp3player.client.views.adapters

import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.bumptech.glide.ListPreloader.PreloadModelProvider
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import com.github.goldy1992.mp3player.client.views.viewholders.EmptyListViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.commons.Constants
import com.l4digital.fastscroll.FastScroller
import java.util.*

abstract class MyGenericRecycleViewAdapter(albumArtPainter: AlbumArtPainter, mainHandler: Handler) : MediaItemRecyclerViewAdapter(albumArtPainter, mainHandler), MediaBrowserResponseListener, FastScroller.SectionIndexer, PreloadModelProvider<MediaBrowserCompat.MediaItem?> {
    val LOG_TAG = "MY_VIEW_ADAPTER"
    val EMPTY_VIEW_TYPE = -1
    private val EMPTY_LIST_ITEM = buildEmptyListMediaItem()
    override fun getItemCount(): Int {
        return if (items == null) 0 else items.size
    }

    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaBrowserCompat.MediaItem>) {
        if (!children.isEmpty()) {
            this.items = children
            mainHandler.post { notifyDataSetChanged() }
        } else {
            addNoChildrenFoundItem()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        if (viewType == EMPTY_VIEW_TYPE) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_item_menu, parent, false)
            return EmptyListViewHolder(view, albumArtPainter)
        }
        return null
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == Constants.FIRST && isEmptyRecycleView) {
            EMPTY_VIEW_TYPE
        } else super.getItemViewType(position)
    }

    private fun addNoChildrenFoundItem() {
        items.add(EMPTY_LIST_ITEM)
        mainHandler.post { notifyDataSetChanged() }
    }

    private fun buildEmptyListMediaItem(): MediaBrowserCompat.MediaItem {
        val mediaDescriptionCompat = MediaDescriptionCompat.Builder()
                .setMediaId(EMPTY_MEDIA_ID)
                .build()
        return MediaBrowserCompat.MediaItem(mediaDescriptionCompat, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }

    protected val isEmptyRecycleView: Boolean
        protected get() = getItems().isEmpty() || getItems()[Constants.FIRST] == EMPTY_LIST_ITEM

    @set:VisibleForTesting
    override var items: List<MediaBrowserCompat.MediaItem?>?
        get() = super.items

    companion object {
        private const val EMPTY_MEDIA_ID = "EMPTY_MEDIA_ID"
    }
}