package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat
import com.bumptech.glide.RequestBuilder
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class SongListAdapter

    @Inject
    constructor(albumArtPainter: AlbumArtPainter)
    : MediaItemListFastScrollListAdapter(albumArtPainter), LogTagger {

    override fun logTag() : String {
        return "MY_VIEW_ADAPTER "
    }

//    override fun getSectionText(position: Int): String {
//        if (hasItems()) {
//            val title = getItem(position).description.title
//            if (null != title) {
//                return title.toString().substring(0, 1)
//            }
//        }
//        return ""
//    }

    override fun getPreloadItems(position: Int): List<MediaBrowserCompat.MediaItem> {
        return if (position >= itemCount) {
            emptyList()
        } else listOf(getItem(position))
    }

    override fun getPreloadRequestBuilder(item: MediaBrowserCompat.MediaItem): RequestBuilder<*>? {
        return albumArtPainter.createPreloadRequestBuilder(item)
    }
}