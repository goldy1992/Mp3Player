package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.views.TrackViewPager
import javax.inject.Inject

/**
 * Created by Mike on 24/09/2017.
 */
class MediaPlayerActivity : MediaActivityCompat() {

    @Inject
    lateinit var trackViewPager : TrackViewPager

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
        //this.mediaActivityCompatComponent.inject(this)
    }

    override fun initialiseView(): Boolean {
        setContentView(R.layout.activity_media_player)
        this.trackViewPager.init(findViewById(R.id.trackViewPager))
        return true
    }

    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return setOf(this, trackViewPager)
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return setOf(trackViewPager)
    }

    /**
     * Callback method for when the activity connects to the MediaPlaybackService
     */
    override fun onConnected() {
        super.onConnected()
        if (null != trackToPlay) {
            mediaControllerAdapter.playFromUri(trackToPlay, null)
        }
     }


    override fun onDestroy() {
        super.onDestroy()
        mediaControllerAdapter.disconnect()
    }

    override fun logTag(): String {
       return "MEDIA_PLAYER_ACTIVITY"
    }

}