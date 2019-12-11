package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.ListPreloader.PreloadModelProvider
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import com.github.goldy1992.mp3player.client.views.viewholders.EmptyListViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.l4digital.fastscroll.FastScroller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


abstract class MyGenericRecycleViewAdapter
    (albumArtPainter: AlbumArtPainter) : MediaItemRecyclerViewAdapter(albumArtPainter),
        MediaBrowserResponseListener,
        FastScroller.SectionIndexer,
        PreloadModelProvider<MediaItem?>,
        LogTagger {

    val LOG_TAG = "MY_VIEW_ADAPTER"
    val EMPTY_VIEW_TYPE = -1
    private val EMPTY_LIST_ITEM = buildEmptyListMediaItem()

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaItem>) {
        if (!children.isEmpty()) {
            this.items = children
            CoroutineScope(Main).launch { notifyDataSetChanged() }
        } else {
            addNoChildrenFoundItem()
        }
    }


    fun createEmptyViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_item_menu, parent, false)
            return EmptyListViewHolder(view, albumArtPainter)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == Constants.FIRST && isEmptyRecycleView) {
            EMPTY_VIEW_TYPE
        } else super.getItemViewType(position)
    }

    private fun addNoChildrenFoundItem() {
        items.add(EMPTY_LIST_ITEM)
        CoroutineScope(Main).launch { notifyDataSetChanged() }
    }

    private fun buildEmptyListMediaItem(): MediaItem {
        val mediaDescriptionCompat = MediaDescriptionCompat.Builder()
                .setMediaId(EMPTY_MEDIA_ID)
                .build()
        return MediaItem(mediaDescriptionCompat, MediaItem.FLAG_PLAYABLE)
    }

    protected val isEmptyRecycleView: Boolean
        protected get() = items.isEmpty() || items!![Constants.FIRST] == EMPTY_LIST_ITEM


    companion object {
        private const val EMPTY_MEDIA_ID = "EMPTY_MEDIA_ID"
    }
}