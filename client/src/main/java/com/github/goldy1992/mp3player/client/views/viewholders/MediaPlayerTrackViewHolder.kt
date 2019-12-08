package com.github.goldy1992.mp3player.client.views.viewholders

import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter

class MediaPlayerTrackViewHolder(itemView: View, private val albumArtPainter: AlbumArtPainter) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView
    private val artist: TextView
    private val albumArt: ImageView
    fun bindMediaItem(item: MediaSessionCompat.QueueItem) {
        val titleText = item.description.title.toString()
        title.text = titleText
        val extras = item.description.extras
        val artistText = extras!!.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
        artist.text = artistText
        val albumArtUri :Uri? = extras.get(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI) as? Uri
        if (null != albumArtUri) {
            albumArtPainter.paintOnView(albumArt, albumArtUri)
        } else {
            val image = extras.getSerializable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART) as ByteArray
            albumArtPainter.paintOnView(albumArt, image)
        }
    }

    init {
        title = itemView.findViewById(R.id.songTitle)
        artist = itemView.findViewById(R.id.songArtist)
        albumArt = itemView.findViewById(R.id.albumArt)
    }
}