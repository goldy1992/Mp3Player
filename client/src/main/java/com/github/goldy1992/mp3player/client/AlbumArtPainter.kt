package com.github.goldy1992.mp3player.client

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.widget.ImageView
import com.bumptech.glide.ListPreloader.PreloadModelProvider
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.github.goldy1992.mp3player.client.views.SquareImageView
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import javax.inject.Inject

class AlbumArtPainter

    @Inject
    constructor(private val requestManager: RequestManager) : LogTagger {

    private val requestOptions: RequestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

    fun paintOnView(imageView: ImageView?, uri: Uri) { /* TODO: add an error drawable image for when the album art is not found:
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error() */
        try {
            requestManager.load(uri).apply(requestOptions).fitCenter().into(imageView!!)
        } catch (ex: Exception) { // TODO: load a default image when the album art if not found
//            Log.e(logTag(), ExceptionUtils.getStackTrace(ex));
        }
    }

    fun paintOnView(imageView: ImageView?, image: ByteArray) {
        try {
            requestManager.load(image).apply(requestOptions).fitCenter().into(imageView!!)
        } catch (ex: Exception) { // TODO: load a default image when the album art if not found
//            Log.e(logTag(), ExceptionUtils.getStackTrace(ex));
        }
    }

    fun paintOnView(imageView: SquareImageView?, image: ByteArray) {
        try {
            requestManager.load(image).apply(requestOptions).fitCenter().into(imageView!!)
        } catch (ex: Exception) { // TODO: load a default image when the album art if not found
//            Log.e(logTag(), ExceptionUtils.getStackTrace(ex));
        }
    }

    fun createPreloader(
            preloadModelProvider: PreloadModelProvider<MediaItem>): RecyclerViewPreloader<MediaItem> {
        val preloadSizeProvider = FixedPreloadSizeProvider<MediaItem>(20, 20)
        return RecyclerViewPreloader<MediaItem>(
                requestManager, preloadModelProvider, preloadSizeProvider, 10 )
    }

    fun createPreloadRequestBuilder(mediaItem: MediaItem?): RequestBuilder<*>? {
        val uri = MediaItemUtils.getAlbumArtUri(mediaItem!!)
        return if (uri != null) requestManager.load(uri).fitCenter() else null
    }

    fun clearView(imageView: ImageView) {
        imageView.setImageDrawable(null)
    }

    override fun logTag(): String {
        return "ALBM_ART_PAINTER";
  }

}