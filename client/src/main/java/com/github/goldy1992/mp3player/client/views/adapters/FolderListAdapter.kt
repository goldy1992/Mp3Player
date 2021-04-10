package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat
import com.bumptech.glide.RequestBuilder
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MetaDataKeys
import dagger.hilt.android.scopes.FragmentScoped
import java.io.File
import javax.inject.Inject

@FragmentScoped
class FolderListAdapter

    @Inject
    constructor(albumArtPainter: AlbumArtPainter)
    : MediaItemListFastScrollListAdapter(albumArtPainter) {

    override fun logTag(): String {
        return "FOLDER_VIEW_ADAPTER"
    }

    override fun getSectionText(position: Int): String {
        if (hasItems()) {
            val extras = getItem(position).description.extras
            if (null != extras) {
                val directory = extras.getSerializable(MetaDataKeys.META_DATA_DIRECTORY) as File
                if (directory.name.isNotEmpty()) {
                    return directory.name.substring(0, 1)
                }
            }
            return Constants.UNKNOWN
        }
        return ""
    }

    override fun getPreloadItems(position: Int): List<MediaBrowserCompat.MediaItem> {
        return emptyList()
    }

    override fun getPreloadRequestBuilder(item: MediaBrowserCompat.MediaItem): RequestBuilder<*>? {
        return null
    }
}