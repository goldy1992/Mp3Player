package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.TrackViewPagerChangeCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.queue.QueueListener
import com.github.goldy1992.mp3player.client.views.adapters.TrackViewAdapter
import kotlinx.android.synthetic.main.activity_media_player.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashSet

/**
 * Created by Mike on 24/09/2017.
 */
class MediaPlayerActivity : MediaActivityCompat() {

    @Inject
    lateinit var trackViewAdapter: TrackViewAdapter

    @Inject
    lateinit var trackViewPagerChangeCallback: TrackViewPagerChangeCallback

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
        return true
    }

    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return Collections.singleton(this)
    }

    override fun mediaControllerListeners(): Set<Listener> {
        val toReturn : MutableSet<Listener> = HashSet()
        toReturn.add(trackViewAdapter)
        toReturn.add(trackViewPagerChangeCallback)
        return toReturn
    }

    /**
     * Callback method for when the activity connects to the MediaPlaybackService
     */
    override fun onConnected() {
        super.onConnected()
        if (null != trackToPlay) {
            mediaControllerAdapter.playFromUri(trackToPlay, null)
        }
        trackViewAdapter.onQueueChanged(mediaControllerAdapter.getQueue()!!)
        trackViewPager.adapter = trackViewAdapter
        trackViewPager.registerOnPageChangeCallback(trackViewPagerChangeCallback)
        trackViewPager.setCurrentItem(mediaControllerAdapter.getCurrentQueuePosition(), false)
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaControllerAdapter.disconnect()
    }

    override fun logTag(): String {
       return "MEDIA_PLAYER_ACTIVITY"
    }

}