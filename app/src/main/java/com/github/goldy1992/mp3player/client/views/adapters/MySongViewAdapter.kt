package com.github.goldy1992.mp3player.client.views.adapters

import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MySongViewHolder
import org.apache.commons.collections4.CollectionUtils
import javax.inject.Inject
import javax.inject.Named

class MySongViewAdapter @Inject constructor(albumArtPainter: AlbumArtPainter, @Named("main") mainHandler: Handler) : MyGenericRecycleViewAdapter(albumArtPainter, mainHandler) {
    private override val LOG_TAG = "MY_VIEW_ADAPTER"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        var vh = super.onCreateViewHolder(parent, viewType)
        if (vh == null) { // create a new views
            val layoutInflater = LayoutInflater.from(parent.context)
            val v = layoutInflater
                    .inflate(R.layout.song_item_menu, parent, false)
            vh = MySongViewHolder(v, albumArtPainter)
        }
        return vh
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