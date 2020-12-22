package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType

abstract class MediaItemRecyclerViewAdapter
    constructor (val albumArtPainter: AlbumArtPainter)
    : ListAdapter<MediaItem, MediaItemViewHolder<ViewBinding>>(MediaItemDiffCallBack()), LogTagger {


    override fun getItemViewType(position: Int): Int {
        return if (position == Constants.FIRST && isEmptyRecycleView) {
            EMPTY_VIEW_TYPE
        } else super.getItemViewType(position)
    }

    protected val isEmptyRecycleView: Boolean
        get() = !hasItems() || getItem(Constants.FIRST) == buildEmptyListMediaItem()


    fun hasItems() : Boolean {
        return itemCount > 0
    }

    override fun logTag(): String {
        return "MED_ITM_RCYCL_VW_ADPR"
    }

    /**
     *
     */
    abstract fun createEmptyViewHolder(parent: ViewGroup): MediaItemViewHolder<ViewBinding>

    class MediaItemDiffCallBack : DiffUtil.ItemCallback<MediaItem>(), LogTagger {
        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            val result = oldItem.mediaId == newItem.mediaId
            Log.e(logTag(), "areItemsTheSame: old item: " + oldItem.mediaId + ", new item: " + newItem.mediaId + result)
            return result
        }

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            val result = oldItem.mediaId == newItem.mediaId
            Log.e(logTag(), "areContentsTheSame: old item: " + oldItem.mediaId + ", new item: " + newItem.mediaId + result)
            return result

        }

        /**
         * @return the name of the log tag given to the class
         */
        override fun logTag(): String {
            return "MediaItemDiffCallBack"
        }
    }

    companion object {
        const val EMPTY_VIEW_TYPE = -1
        private const val EMPTY_MEDIA_ID = "EMPTY_MEDIA_ID"

        fun buildEmptyListMediaItem(id: String = EMPTY_MEDIA_ID): MediaItem {
            return MediaItemBuilder(id)
                    .setMediaItemType(MediaItemType.NONE)
                    .build()
         }
    }


}