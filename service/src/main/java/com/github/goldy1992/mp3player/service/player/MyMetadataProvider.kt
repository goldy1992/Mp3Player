package com.github.goldy1992.mp3player.service.player

import android.support.v4.media.MediaMetadataCompat
import com.github.goldy1992.mp3player.commons.Constants.UNKNOWN
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getAlbumArtPath
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getArtist
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDuration
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.PlaylistManager
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.MediaMetadataProvider
import javax.inject.Inject

@ComponentScope
class MyMetadataProvider
    @Inject
    constructor(private val playlistManager: PlaylistManager)
    : MediaMetadataProvider {

    override fun getMetadata(player: Player): MediaMetadataCompat? {
        val currentIndex = player.currentWindowIndex
        val currentItem = playlistManager.getItemAtIndex(currentIndex) ?: return null
        val builder = MediaMetadataCompat.Builder()
        builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, getDuration(currentItem))
        val mediaId = getMediaId(currentItem)
        builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
        val title = getTitle(currentItem)
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, title ?: UNKNOWN)
        val artist = getArtist(currentItem)
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist ?: UNKNOWN)
        val albumArt = getAlbumArtPath(currentItem)
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, albumArt)
        return builder.build()
    }

}