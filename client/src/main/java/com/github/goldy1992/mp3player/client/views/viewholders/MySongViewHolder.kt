package com.github.goldy1992.mp3player.client.views.viewholders

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.databinding.SongItemMenuBinding
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MetaDataKeys
import org.apache.commons.io.FilenameUtils

class MySongViewHolder(private val view: SongItemMenuBinding, albumArtPainter: AlbumArtPainter?)
    : MediaItemViewHolder(view.root, albumArtPainter) {


    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) { // - get element from your dataset at this position
// - replace the contents of the views with that element
        val title : String? = extractTitle(item)
        val artist : String? = extractArtist(item)
        val duration : String? = extractDuration(item)
        view.artist.text = artist
        view.title.text = title
        view.duration.text = duration
        val uri : Uri? = MediaItemUtils.getAlbumArtUri(item)
        if (null != uri) {
            albumArtPainter!!.paintOnView(view.albumArt, uri)
        }
    }

    private fun extractTitle(song: MediaBrowserCompat.MediaItem): String {
        val charSequence: CharSequence? = MediaItemUtils.getTitle(song)
        return if (null == charSequence) {
            val fileName : String? = MediaItemUtils.getExtra(MetaDataKeys.META_DATA_KEY_FILE_NAME, song) as? String
            if (fileName == null)
                Constants.UNKNOWN else FilenameUtils.removeExtension(fileName)
            } else {
             charSequence.toString()
        }

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
        var artist: String?
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


}