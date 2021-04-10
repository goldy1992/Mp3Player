package com.github.goldy1992.mp3player.client.views.adapters

import android.content.Context
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MyFolderViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MySongViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.RootItemViewHolder
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.databinding.FolderItemMenuBinding
import com.github.goldy1992.mp3player.databinding.RootItemMenuBinding
import com.github.goldy1992.mp3player.databinding.SongItemMenuBinding

/**
 * A [ListAdapter] that's used to created the required [RecyclerView.ViewHolder] for each type
 * [MediaItem], represented by the [MediaItemType].
 */
abstract class MediaItemListRecyclerViewAdapter
    constructor (val albumArtPainter: AlbumArtPainter)
    : ListAdapter<MediaItem, MediaItemViewHolder<ViewBinding>>(MediaItemDiffCallBack()), LogTagger {

    /**
     * Called by [RecyclerView] to create the [RecyclerView.ViewHolder] for each [MediaItem] in the
     * list.
     * @param parent The parent [ViewGroup].
     * @param viewType The value will be the [MediaItemType] of the [MediaItem] to be used determine
     * the [RecyclerView.ViewHolder] to be inflated.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : MediaItemViewHolder<ViewBinding> {
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

    /**
     * @param holder The [MediaItemViewHolder] that will have a [MediaItem] bound to.
     * @param position The position of the [MediaItem] in the list that needs to be bound to the
     * [holder].
     */
    override fun onBindViewHolder(holder: MediaItemViewHolder<ViewBinding>, position: Int) {
        val item = getItem(position)
        Log.e(logTag(), "on bind view holder, item: " + item.mediaId)
        holder.bindMediaItem(item)
    }

    /**
     * Determines the [RecyclerView.ViewHolder]'s view type given the [MediaItemType] of the item
     * at [position].
     * @param position The position of the [MediaItem] in the item list.
     */
    override fun getItemViewType(position: Int): Int {
        val mediaItem = getItem(position)
        val mediaItemType : MediaItemType? = MediaItemUtils.getMediaItemType(mediaItem)
        return mediaItemType?.value ?: super.getItemViewType(position)
    }

    /**
     * @return True if [getItemCount] is greater than 0, false otherwise.
     */
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
        private const val EMPTY_MEDIA_ID = "EMPTY_MEDIA_ID"

        fun buildEmptyListMediaItem(id: String = EMPTY_MEDIA_ID): MediaItem {
            return MediaItemBuilder(id)
                    .setMediaItemType(MediaItemType.NONE)
                    .build()
         }
    }


}