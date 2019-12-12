package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.VisibleForTesting
import com.bumptech.glide.Glide
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.callbacks.TrackViewPagerChangeCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.views.adapters.TrackViewAdapter
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.android.synthetic.main.activity_media_player.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

/**
 * Created by Mike on 24/09/2017.
 */
abstract class MediaPlayerActivity : MediaActivityCompat(), MetadataListener, LogTagger {

    private var trackViewAdapter: TrackViewAdapter? = null
    private var trackViewPagerChangeCallback: TrackViewPagerChangeCallback? = null

    @get:VisibleForTesting
    var trackToPlay: Uri? = null
        private set

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {
            trackToPlay = intent.data
        }
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Intent.ACTION_VIEW == intent.action) {
            trackToPlay = intent.data
            mediaControllerAdapter!!.playFromUri(trackToPlay, null)
        }
    }

    override fun initialiseView(layoutId: Int): Boolean {
        setContentView(layoutId)
        val fm = supportFragmentManager

        val context = applicationContext
        val albumArtPainter = AlbumArtPainter(Glide.with(context))
        trackViewPagerChangeCallback = TrackViewPagerChangeCallback(mediaControllerAdapter!!)
        trackViewAdapter = TrackViewAdapter(albumArtPainter, Handler(mainLooper), mediaControllerAdapter!!.queue)
        mediaControllerAdapter!!.registerMetaDataListener(this)
        trackViewPager.setAdapter(trackViewAdapter)
        trackViewPager.registerOnPageChangeCallback(trackViewPagerChangeCallback!!)
        trackViewPager.setCurrentItem(mediaControllerAdapter!!.currentQueuePosition, false)
        return true
    }

    /**
     * Callback method for when the activity connects to the MediaPlaybackService
     */
    override fun onConnected() {
        super.onConnected()
        if (null != trackToPlay) {
            mediaControllerAdapter!!.playFromUri(trackToPlay, null)
        }
        initialiseView(R.layout.activity_media_player)
    }

    override fun onMetadataChanged(metadata: MediaMetadataCompat) { // TODO: in future this should be a listener for when the queue has changed
        val queueItems = mediaControllerAdapter.queue
        CoroutineScope(Main).launch {
            val currentPosition = mediaControllerAdapter.currentQueuePosition
            trackViewPagerChangeCallback!!.currentPosition = currentPosition
            trackViewPager!!.setCurrentItem(currentPosition, false)
        }
        trackViewAdapter!!.updateQueue(queueItems!!)

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaControllerAdapter!!.disconnect()
    }

    override fun logTag(): String {
       return "MEDIA_PLAYER_ACTIVITY"
    }

}