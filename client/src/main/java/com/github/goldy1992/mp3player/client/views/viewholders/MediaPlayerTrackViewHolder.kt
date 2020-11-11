package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.databinding.ViewHolderMediaPlayerBinding

class MediaPlayerTrackViewHolder(private val view: ViewHolderMediaPlayerBinding, private val albumArtPainter: AlbumArtPainter, private  val context : Context)
    : RecyclerView.ViewHolder(view.root) {


    fun bindMediaItem(item: MediaSessionCompat.QueueItem) {
        val extras = item.description.extras
        val albumArtUri: Uri? = extras?.get(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI) as? Uri
        if (null != albumArtUri) {
            albumArtPainter.paintOnView(view.albumArt, albumArtUri)
        } else {
            val image = extras?.getSerializable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART) as ByteArray
            albumArtPainter.paintOnView(view.albumArt, image)
        }

    }

}