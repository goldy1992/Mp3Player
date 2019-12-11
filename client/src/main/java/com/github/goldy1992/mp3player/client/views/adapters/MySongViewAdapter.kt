package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MySongViewHolder
import com.github.goldy1992.mp3player.commons.LogTagger
import org.apache.commons.collections4.CollectionUtils
import javax.inject.Inject

class MySongViewAdapter
    @Inject
    constructor(albumArtPainter: AlbumArtPainter)
    : MyGenericRecycleViewAdapter(albumArtPainter), LogTagger {

    override fun logTag() : String {
        return "MY_VIEW_ADAPTER"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        return if (viewType == EMPTY_VIEW_TYPE) {
            createEmptyViewHolder(parent, viewType)
        } else { // create a new views
            val layoutInflater = LayoutInflater.from(parent.context)
            val v = layoutInflater
                    .inflate(R.layout.song_item_menu, parent, false)
            MySongViewHolder(v, albumArtPainter)
        }
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val isSongHolder = holder is MySongViewHolder
        if (isSongHolder && !isEmptyRecycleView) {
            val songViewHolder = holder as MySongViewHolder
            // TODO: look into the use of holder.getAdapterPosition rather than the position parameter.
//Log.i(LOG_TAG, "position: " + position);
            val song = items!![holder.getAdapterPosition()]!!
            songViewHolder.bindMediaItem(song)
        }
    }

    override fun getSectionText(position: Int): String {
        if (CollectionUtils.isNotEmpty(items)) {
            val title = items!![position]!!.description.title
            if (null != title) {
                return title.toString().substring(0, 1)
            }
        }
        return ""
    }

    override fun getPreloadItems(position: Int): List<MediaBrowserCompat.MediaItem> {
        return if (position >= items!!.size) {
            emptyList()
        } else listOf(items!![position])
    }

    override fun getPreloadRequestBuilder(item: MediaBrowserCompat.MediaItem): RequestBuilder<*>? {
        return albumArtPainter.createPreloadRequestBuilder(item)
    }
}