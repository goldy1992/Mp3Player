package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.VisibleForTesting
import com.bumptech.glide.Glide
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.callbacks.TrackViewPagerChangeCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.queue.QueueListener
import com.github.goldy1992.mp3player.client.views.adapters.TrackViewAdapter
import kotlinx.android.synthetic.main.activity_media_player.*

/**
 * Created by Mike on 24/09/2017.
 */
class MediaPlayerActivity : MediaActivityCompat(), MetadataListener, QueueListener {

    private var trackViewAdapter: TrackViewAdapter? = null
    private var trackViewPagerChangeCallback: TrackViewPagerChangeCallback? = null

    @get:VisibleForTesting
    var trackToPlay: Uri? = null
        private set

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {
            trackToPlay = intent.data
        }
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Intent.ACTION_VIEW == intent.action) {
            trackToPlay = intent.data
            mediaControllerAdapter.playFromUri(trackToPlay, null)
        }
    }

    override fun initialiseDependencies() {
        super.initialiseDependencies()
        this.mediaActivityCompatComponent.inject(this)
    }

    override fun initialiseView(): Boolean {
        setContentView(R.layout.activity_media_player)
        val context = applicationContext
        val albumArtPainter = AlbumArtPainter(Glide.with(context))
        trackViewPagerChangeCallback = TrackViewPagerChangeCallback(mediaControllerAdapter)
        trackViewAdapter = TrackViewAdapter(albumArtPainter, mediaControllerAdapter)
        mediaControllerAdapter.registerListener(this)
        mediaControllerAdapter.registerListener(trackViewAdapter!!)
        trackViewPager.adapter = trackViewAdapter
        trackViewPager.registerOnPageChangeCallback(trackViewPagerChangeCallback!!)
        return true
    }

    /**
     * Callback method for when the activity connects to the MediaPlaybackService
     */
    override fun onConnected() {
        super.onConnected()
        if (null != trackToPlay) {
            mediaControllerAdapter.playFromUri(trackToPlay, null)
        }
        // update state of GUI
   //     initialiseView(R.layout.activity_media_player)
    }

    override fun onMetadataChanged(metadata: MediaMetadataCompat) { // TODO: in future this should be a listener for when the queue has changed
        val queueItems = mediaControllerAdapter.getQueue()
        val currentPosition = mediaControllerAdapter.getCurrentQueuePosition()
        trackViewPagerChangeCallback!!.currentPosition = currentPosition
        trackViewPager!!.setCurrentItem(currentPosition, false)
        trackViewAdapter!!.updateQueue(queueItems!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaControllerAdapter.disconnect()
    }

    override fun logTag(): String {
       return "MEDIA_PLAYER_ACTIVITY"
    }

    override fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem>) {
        trackViewPager.setCurrentItem(mediaControllerAdapter.getCurrentQueuePosition(), false)
    }

}