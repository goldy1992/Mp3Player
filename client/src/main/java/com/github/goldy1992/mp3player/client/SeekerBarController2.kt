package com.github.goldy1992.mp3player.client

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.views.SeekerBar
import com.github.goldy1992.mp3player.client.views.TimeCounter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 *
 */
@FragmentScoped
class SeekerBarController2
    @Inject
    constructor(private val mediaControllerAdapter: MediaControllerAdapter?,
                    private val timeCounter: TimeCounter)
    : AnimatorUpdateListener, OnSeekBarChangeListener, LogTagger {

    private lateinit var seekerBar: SeekerBar

    @PlaybackStateCompat.State
    private var currentState = PlaybackStateCompat.STATE_PAUSED
    private var currentPlaybackSpeed = Constants.DEFAULT_SPEED
    private var currentPosition = Constants.DEFAULT_POSITION
    private var currentSongDuration: Long = 0
    private var lastKnownPosition : Long = 0
    var isLooping = false
        private set
    @get:VisibleForTesting
    var valueAnimator: ValueAnimator? = null
        private set

    fun onPlaybackStateChanged(state: PlaybackStateCompat) { //LoggingUtils.logPlaybackStateCompat(state, LOG_TAG);
        setLooping()
        currentState = state.state
        val position = state.position
        if (validPosition(position)) {
            currentPosition = position.toInt()
        }
        val speed = state.playbackSpeed
        val speedChanged = speed != currentPlaybackSpeed
        if (speedChanged) {
            currentPlaybackSpeed = speed
        }
        createAnimator()
        updateValueAnimator()
    }

    /**
     *
     * @param metadata the metadata object
     */
    fun onMetadataChanged(metadata: MediaMetadataCompat?) {
        Log.i(logTag(), "meta data change")
        val max = metadata?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)?.toInt() ?: 0
        seekerBar.max = max
        currentSongDuration = max.toLong()
        createAnimator()
        updateValueAnimator()
    }

    private fun updateValueAnimator() {
        val newPosition = positionAsFraction
        //Log.d(LOG_TAG, "new fraction: " + newPosition);
        valueAnimator!!.setCurrentFraction(newPosition)

        if (!valueAnimator!!.isStarted) {
            valueAnimator!!.start()
        }

        when (currentState) {
            PlaybackStateCompat.STATE_PAUSED -> valueAnimator!!.pause()
            PlaybackStateCompat.STATE_PLAYING -> valueAnimator!!.resume()
            else -> {
            }
        }

    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) { //     Log.i(LOG_TAG, "animation update from: " + valueAnimator);
        val animatedIntValue = valueAnimator.animatedValue as Int
        seekerBar!!.progress = animatedIntValue
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        Log.i(logTag(), "START TRACKING")
        timeCounter.cancelTimerDuringTracking()
        removeValueAnimator()
        setTracking(seekBar, true)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        setTracking(seekBar, false)
        Log.i(logTag(), "Stop TRACKING")
        currentPosition = seekBar.getProgress()
        mediaControllerAdapter!!.seekTo(currentPosition.toLong())
        createAnimator()
    }

    private fun setTracking(seekBar: SeekBar, tracking: Boolean) {
        if (seekBar is SeekerBar) {
            seekBar.isTracking = tracking
        }
    }

    private fun createAnimator() {
        try {
            val duration = (seekerBar!!.max / currentPlaybackSpeed).toInt()
            val valueAnimator = ValueAnimator.ofInt(0, seekerBar!!.max)
            valueAnimator.duration = duration.toLong()
            valueAnimator.addUpdateListener(this)
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.repeatCount = if (isLooping) ValueAnimator.INFINITE else 0
            if (currentPosition >= 0 && seekerBar!!.max > currentPosition) {
                valueAnimator.setCurrentFraction(positionAsFraction)
            }
            removeValueAnimator()
            seekerBar!!.valueAnimator =valueAnimator
            this.valueAnimator = valueAnimator
        } catch (ex: IllegalArgumentException) {
            Log.e(logTag(), "seekerbar Max: $currentSongDuration")
            throw IllegalArgumentException(ex)
        }
    }

    private fun setLooping() {
        val repeatMode = mediaControllerAdapter?.repeatMode?.value
        isLooping = repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE
        if (null != valueAnimator) {
            valueAnimator!!.repeatCount = if (isLooping) ValueAnimator.INFINITE else 0
        }

    }

    private fun removeValueAnimator() {
        seekerBar?.valueAnimator?.removeAllUpdateListeners()
        seekerBar?.valueAnimator?.cancel()
        seekerBar?.valueAnimator = null
    }

    /**
     * @param position the position to be compared
     * @return true if the position is greater than or equal to zero and less than or equal to the
     * maximum value of the current seeker bar
     */
    private fun validPosition(position: Long): Boolean {
        // Log.i(LOG_TAG, "position: " + position + ", valid: " + valid);
        return position >= 0 && position <= seekerBar!!.max
    }

    //Log.d(LOG_TAG, "current pos: " + currentPosition + ", seekerbar max" + seekerBar.getMax());
    private val positionAsFraction: Float
        get() =//Log.d(LOG_TAG, "current pos: " + currentPosition + ", seekerbar max" + seekerBar.getMax());
            currentPosition / seekerBar!!.max.toFloat()

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val updateTimer = seekBar is SeekerBar && seekBar.isTracking
        if (updateTimer) { //Log.i(LOG_TAG, "PROGRESS CHANGED");
            timeCounter.seekTo(progress.toLong())
        }
    }

    /**
     * setter method automatically associates the on seeker bar change listener to be the controller
     * and therefore cannot be null
     * @param seekerBar the seeker bar
     */
    fun init(seekerBar: SeekerBar) {
        this.seekerBar = seekerBar
        this.seekerBar.max = 0
        this.seekerBar.setOnSeekBarChangeListener(this)
    }

    override fun logTag(): String {
        return "SKR_MDIA_CNTRLR_CLBK"
    }

}