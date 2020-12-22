package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.RequestBuilder
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.views.viewholders.MediaItemViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MyFolderViewHolder
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MetaDataKeys
import com.github.goldy1992.mp3player.databinding.FolderItemMenuBinding
import dagger.hilt.android.scopes.FragmentScoped
import java.io.File
import javax.inject.Inject

@FragmentScoped
class MyFolderViewAdapter

    @Inject
    constructor(albumArtPainter: AlbumArtPainter)
    : MyGenericRecyclerViewAdapter(albumArtPainter) {

    override fun logTag(): String {
        return "FOLDER_VIEW_ADAPTER"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder<ViewBinding> {
        return if (viewType == Companion.EMPTY_VIEW_TYPE) {
            createEmptyViewHolder(parent)
        } else { // create a new views
            val view = FolderItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MyFolderViewHolder(parent.context, view, albumArtPainter)
        }
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder<ViewBinding>, position: Int) {
        val isFolderHolder = holder is MyFolderViewHolder
        if (isFolderHolder && !isEmptyRecycleView) {
            val folderViewHolder = holder as MyFolderViewHolder
            //Log.i(LOG_TAG, "position: " + position);
            val song = getItem(position)
            folderViewHolder.bindMediaItem(song)
        }
    }

    override fun getSectionText(position: Int): String {
        val extras = getItem(position).description.extras
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