package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.VisibleForTesting
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.callbacks.TrackViewPagerChangeCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.views.adapters.TrackViewAdapter
import com.github.goldy1992.mp3player.client.views.fragments.MediaControlsFragment
import com.github.goldy1992.mp3player.client.views.fragments.PlayToolBarFragment
import com.github.goldy1992.mp3player.client.views.fragments.PlaybackTrackerFragment

/**
 * Created by Mike on 24/09/2017.
 */
abstract class MediaPlayerActivity : MediaActivityCompat(), MetadataListener {
    private val LOG_TAG = "MEDIA_PLAYER_ACTIVITY"
    @get:VisibleForTesting
    var playbackTrackerFragment: PlaybackTrackerFragment? = null
        private set
    @get:VisibleForTesting
    var playToolBarFragment: PlayToolBarFragment? = null
        private set
    @get:VisibleForTesting
    var mediaControlsFragment: MediaControlsFragment? = null
        private set
    private var viewPager2: ViewPager2? = null
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

    public override fun initialiseView(layoutId: Int): Boolean {
        setContentView(layoutId)
        val fm = supportFragmentManager
        viewPager2 = findViewById(R.id.track_view_pager)
        val context = applicationContext
        val albumArtPainter = AlbumArtPainter(Glide.with(context))
        trackViewPagerChangeCallback = TrackViewPagerChangeCallback(mediaControllerAdapter)
        trackViewAdapter = TrackViewAdapter(albumArtPainter, Handler(mainLooper), mediaControllerAdapter!!.queue)
        mediaControllerAdapter!!.registerMetaDataListener(this)
        viewPager2.setAdapter(trackViewAdapter)
        viewPager2.registerOnPageChangeCallback(trackViewPagerChangeCallback!!)
        viewPager2.setCurrentItem(mediaControllerAdapter!!.currentQueuePosition, false)
        playbackTrackerFragment = fm.findFragmentById(R.id.playbackTrackerFragment) as PlaybackTrackerFragment?
        playToolBarFragment = fm.findFragmentById(R.id.playbackToolbarExtendedFragment) as PlayToolBarFragment?
        mediaControlsFragment = fm.findFragmentById(R.id.mediaControlsFragment) as MediaControlsFragment?
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
        val queueItems = mediaControllerAdapter!!.queue
        val currentPosition = mediaControllerAdapter!!.currentQueuePosition
        trackViewPagerChangeCallback!!.currentPosition = currentPosition
        viewPager2!!.setCurrentItem(currentPosition, false)
        trackViewAdapter!!.setQueue(queueItems)
    }

    /**
     * {@inheritDoc}
     */
    override val workerId: String
        get() = "MDIA_PLYER_ACTVT_WKR"

    override fun onDestroy() {
        super.onDestroy()
        getMediaControllerAdapter().disconnect()
    }

}