package com.github.goldy1992.mp3player.client.views.adapters

import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MyFolderViewHolder
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MetaDataKeys
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class MyFolderViewAdapter
    @Inject
    constructor(albumArtPainter: AlbumArtPainter)
    : MyGenericRecycleViewAdapter(albumArtPainter) {

    override fun logTag(): String {
        return "FOLDER_VIEW_ADAPTER"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        return if (viewType == EMPTY_VIEW_TYPE) {
            createEmptyViewHolder(parent, viewType)
        } else { // create a new views
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.folder_item_menu, parent, false)
            MyFolderViewHolder(view, albumArtPainter)
        }
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        val isFolderHolder = holder is MyFolderViewHolder
        if (isFolderHolder && !isEmptyRecycleView) {
            val folderViewHolder = holder as MyFolderViewHolder
            //Log.i(LOG_TAG, "position: " + position);
            val song = items[holder.getAdapterPosition()]
            folderViewHolder.bindMediaItem(song)
        }
    }

    override fun getSectionText(position: Int): String {
        val extras = items[position].description.extras
        if (null != extras) {
            val directory = extras.getSerializable(MetaDataKeys.META_DATA_DIRECTORY) as File
            if (directory.name.isNotEmpty()) {
                return directory.name.substring(0, 1)
            }
        }
        return Constants.UNKNOWN
    }

    override fun getPreloadItems(position: Int): List<MediaBrowserCompat.MediaItem> {
        return emptyList()
    }

    override fun getPreloadRequestBuilder(item: MediaBrowserCompat.MediaItem): RequestBuilder<*>? {
        return null
    }
}