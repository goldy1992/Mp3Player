package com.github.goldy1992.mp3player.service.player

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaUri
import com.github.goldy1992.mp3player.service.PlaylistManager
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ForwardingPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import org.apache.commons.collections4.CollectionUtils
import java.util.*
import javax.inject.Inject

class MyPlaybackPreparer @Inject constructor(private val exoPlayer: ExoPlayer,
                                             private val contentManager: ContentManager,
                                             private val mediaSourceFactory: MediaSourceFactory,
                                             private val myControlDispatcher: ForwardingPlayer,
                                             private val playlistManager: PlaylistManager)
    : MediaSessionConnector.PlaybackPreparer {
    override fun getSupportedPrepareActions(): Long {
        return MediaSessionConnector.PlaybackPreparer.ACTIONS
    }

    override fun onPrepare(playWhenReady: Boolean) {
        Log.i(LOG_TAG, "called onPrepare, play when ready: $playWhenReady")
        val mediaItems = playlistManager.playlist
        if (CollectionUtils.isNotEmpty(mediaItems)) {
            val currentMediaItem = playlistManager.currentItem
            if (null != currentMediaItem) {
                val currentId = currentMediaItem.mediaId
                preparePlaylist(playWhenReady, currentId, mediaItems!!)
            }
        }
    }

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        // TODO: extract playlist from trackID
        val trackId = extractTrackId(mediaId)
        if (null != trackId) {
            val results = contentManager.getPlaylist(mediaId)
            playlistManager.createNewPlaylist(results)
            preparePlaylist(playWhenReady, trackId, results!!.toMutableList())
        }
    }

    private fun preparePlaylist(playWhenReady: Boolean, trackId: String?, results: MutableList<MediaBrowserCompat.MediaItem>?) {
        if (null != results) {
            val concatenatingMediaSource = ConcatenatingMediaSource()
            val resultsIterator = results.listIterator()
            while (resultsIterator.hasNext()) {
                val currentMediaItem = resultsIterator.next()
                val currentUri = getMediaUri(currentMediaItem)
                val src = mediaSourceFactory.createMediaSource(currentUri!!)
                if (null != src) {
                    concatenatingMediaSource.addMediaSource(src)
                } else {
                    resultsIterator.remove()
                }
            }
            val uriToPlayIndex = getIndexOfCurrentTrack(trackId, results)
            if (concatenatingMediaSource.size > 0) {
                exoPlayer.setMediaSource(concatenatingMediaSource)
                exoPlayer.prepare()
                myControlDispatcher.seekTo(uriToPlayIndex, 0L)
                myControlDispatcher.playWhenReady = playWhenReady
            }
        } // if
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
        throw UnsupportedOperationException()
    }

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) {
        val result = contentManager.getItem(uri)
        val playlist: MutableList<MediaBrowserCompat.MediaItem> = ArrayList()
        playlist.add(result!!)
        playlistManager.createNewPlaylist(playlist)
        preparePlaylist(playWhenReady, getMediaId(result), playlist)
    }

    override fun onCommand(player: Player, controlDispatcher: ControlDispatcher, command: String, extras: Bundle?, cb: ResultReceiver?): Boolean {
        return false
    }

    private fun extractTrackId(mediaId: String?): String? {
        if (null != mediaId) {
            val splitId = Arrays.asList<String>(*mediaId.split(ID_SEPARATOR).toTypedArray())
            if (!splitId.isEmpty()) {
                return splitId[splitId.size - 1]
            }
        } else {
            Log.e(LOG_TAG, "received null mediaId")
        }
        return null
    }

    private fun getIndexOfCurrentTrack(trackId: String?, items: List<MediaBrowserCompat.MediaItem?>): Int {
        for (i in items.indices) {
            val currentMediaItem = items[i]
            val id = getMediaId(currentMediaItem)
            if (id != null && id == trackId) {
                return i
            } // if
        } // for
        return 0
    }

    companion object {
        private const val LOG_TAG = "PLAYBACK_PREPARER"
    }

    init {
        val currentPlaylist = playlistManager.playlist
        if (CollectionUtils.isNotEmpty(currentPlaylist)) {
            val trackId = getMediaId(currentPlaylist!![0])
            preparePlaylist(false, trackId, currentPlaylist)
        }
    }
}