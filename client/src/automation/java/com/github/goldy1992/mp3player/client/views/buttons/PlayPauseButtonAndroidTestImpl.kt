package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import javax.inject.Inject

class PlayPauseButtonAndroidTestImpl

    @Inject
    constructor(context : Context,
                mediaControllerAdapter: MediaControllerAdapter)
    : PlayPauseButton(context, mediaControllerAdapter), IdlingResource {


    private val counterName : String = "playPauseButtonIdleCounter"
    private var resourceCallback : IdlingResource.ResourceCallback? = null

    private var isIdle = true



    @VisibleForTesting
    override fun onClick(view: View?) {
        super.onClick(view)
        this.isIdle = false
    }


    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        super.onPlaybackStateChanged(state)
        this.isIdle = true
    }

    override fun getName(): String {
        return "PlayPauseButtonIdlingResource"
    }

    override fun isIdleNow(): Boolean {
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

}