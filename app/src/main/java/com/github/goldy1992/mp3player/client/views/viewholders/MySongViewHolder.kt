package com.github.goldy1992.mp3player.client.views.viewholders

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MetaDataKeys
import org.apache.commons.io.FilenameUtils

class MySongViewHolder(itemView: View, albumArtPainter: AlbumArtPainter?) : MediaItemViewHolder(itemView, albumArtPainter) {
    @get:VisibleForTesting
    val title: TextView
    @get:VisibleForTesting
    val artist: TextView
    @get:VisibleForTesting
    val duration: TextView
    @get:VisibleForTesting
    val albumArt: ImageView

    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) { // - get element from your dataset at this position
// - replace the contents of the views with that element
        val title = extractTitle(item)
        val artist = extractArtist(item)
        val duration = extractDuration(item)
        this.artist.text = artist
        this.title.text = title
        this.duration.text = duration
        val uri = MediaItemUtils.getAlbumArtUri(item)
        albumArtPainter!!.paintOnView(albumArt, uri)
    }

    private fun extractTitle(song: MediaBrowserCompat.MediaItem): String {
        val charSequence: CharSequence? = MediaItemUtils.getTitle(song)
        if (null == charSequence) {
            val fileName = if (MediaItemUtils.hasExtras(song)) MediaItemUtils.getExtra(MetaDataKeys.META_DATA_KEY_FILE_NAME, song) as String else null
            if (fileName != null) {
                return FilenameUtils.removeExtension(fileName)
            }
        } else {
            return charSequence.toString()
        }
        return Constants.UNKNOWN
    }

    private fun extractDuration(song: MediaBrowserCompat.MediaItem): String? {
        val extras = song.description.extras
        if (null != extras) {
            val duration = extras.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
            return formatTime(duration)
        }
        return null
    }

    private fun extractArtist(song: MediaBrowserCompat.MediaItem): String? {
        var artist: String? = null
        try {
            artist = MediaItemUtils.getArtist(song)
            if (null == artist) {
                artist = Constants.UNKNOWN
            }
        } catch (ex: NullPointerException) {
            artist = Constants.UNKNOWN
        }
        return artist
    }

    init {
        artist = itemView.findViewById(R.id.artist)
        title = itemView.findViewById(R.id.title)
        duration = itemView.findViewById(R.id.duration)
        albumArt = itemView.findViewById(R.id.song_item_album_art)
    }
}