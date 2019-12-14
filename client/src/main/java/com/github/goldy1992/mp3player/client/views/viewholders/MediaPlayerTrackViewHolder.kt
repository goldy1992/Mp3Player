package com.github.goldy1992.mp3player.client.views.viewholders

import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_media_player.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class MediaPlayerTrackViewHolder(itemView: View, private val albumArtPainter: AlbumArtPainter)
    : RecyclerView.ViewHolder(itemView), LayoutContainer {

    override val containerView: View?
        get() = itemView

    fun bindMediaItem(item: MediaSessionCompat.QueueItem) {

            val titleText = item.description.title.toString()
            itemView.songTitle.text = titleText
            val extras = item.description.extras
            val artistText = extras!!.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
            itemView.songArtist.text = artistText
            val albumArtUri: Uri? = extras.get(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI) as? Uri
            if (null != albumArtUri) {
                albumArtPainter.paintOnView(itemView.albumArt, albumArtUri)
            } else {
                val image = extras.getSerializable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART) as ByteArray
                albumArtPainter.paintOnView(itemView.albumArt, image)
            }

    }

}