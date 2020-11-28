package com.github.goldy1992.mp3player.client

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Observer
import com.github.goldy1992.mp3player.client.views.TimeCounter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.android.material.slider.Slider
import dagger.hilt.android.scopes.FragmentScoped
import org.apache.commons.lang3.exception.ExceptionUtils
import javax.inject.Inject

/**
 *
 */
@FragmentScoped
class SeekerBarController2
    @Inject
    constructor(private val mediaControllerAdapter: MediaControllerAdapter,
                    private val timeCounter: TimeCounter )
    : AnimatorUpdateListener, Slider.OnSliderTouchListener, Slider.OnChangeListener, LogTagger, Observer<Any> {

    private lateinit var seekerBar: Slider

    @PlaybackStateCompat.State
    private var currentState = PlaybackStateCompat.STATE_PAUSED
    private var currentPlaybackSpeed = Constants.DEFAULT_SPEED
    private var currentPosition : Float = Constants.DEFAULT_POSITION.toFloat()
    private var currentSongDuration: Float = 0f
    private var isLooping = false
    private var isTracking = false

    private var valueAnimator: ValueAnimator? = null

    private fun onPlaybackStateChanged(state: PlaybackStateCompat) { //LoggingUtils.logPlaybackStateCompat(state, LOG_TAG);
        this.currentState = state.state
        this.currentPosition = state.position.toFloat()
        this.currentPlaybackSpeed = state.playbackSpeed
        replaceAnimator()
    }

    /**
     * @param metadata the metadata object
     */
    private fun onMetadataChanged(metadata: MediaMetadataCompat?) {
        Log.i(logTag(), "meta data change")
        val max = metadata?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)?.toFloat() ?: 0f
        this.seekerBar.valueTo = max
        this.currentSongDuration = max
        replaceAnimator()
    }

    private fun onRepeatModeChanged(repeatMode : Int) {
        this.isLooping = repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE
        this.valueAnimator?.repeatCount = if (this.isLooping) ValueAnimator.INFINITE else 0
    }

    override fun onChanged(t: Any?) {
        when(t) {
            is PlaybackStateCompat -> onPlaybackStateChanged(t)
            is MediaMetadataCompat -> onMetadataChanged(t)
            is Int -> onRepeatModeChanged(t)
            else -> {}
        }
    }

    override fun onAnimationUpdate(valueAnimator: ValueAnimator) { //     Log.i(LOG_TAG, "animation update from: " + valueAnimator);
        val animatedIntValue = (valueAnimator.animatedValue as Int).toFloat()
        seekerBar.value = animatedIntValue
    }

    private fun replaceAnimator() {
        removeValueAnimator()
        this.valueAnimator = createAnimator()
    }

    private fun createAnimator() : ValueAnimator? {
        return try {
            if (!validPosition()) {
                return null
            }

            val duration = (this.currentSongDuration / this.currentPlaybackSpeed).toInt()
            val newValueAnimator = ValueAnimator.ofInt(0, this.currentSongDuration.toInt())
            newValueAnimator.duration = duration.toLong()
            newValueAnimator.addUpdateListener(this)
            newValueAnimator.interpolator = LinearInterpolator()
            newValueAnimator.repeatCount = if (this.isLooping) ValueAnimator.INFINITE else 0
            if (currentPosition >= 0 && this.currentSongDuration >= currentPosition) {
                newValueAnimator.setCurrentFraction(currentPosition / seekerBar.valueTo)
            }

            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                newValueAnimator.start()
                newValueAnimator.resume()
            }

            newValueAnimator
        } catch (ex: IllegalArgumentException) {
            Log.e(logTag(), ExceptionUtils.getStackTrace(ex))
            null
        }
    }

    private fun removeValueAnimator() {
        valueAnimator?.removeAllUpdateListeners()
        valueAnimator?.cancel()
    }

    /**
     * @param position the position to be compared
     * @return true if the position is greater than or equal to zero and less than or equal to the
     * maximum value of the current seeker bar
     */
    private fun validPosition() : Boolean {
        // Log.i(LOG_TAG, "position: " + position + ", valid: " + valid);
        return this.currentPosition in 0f..this.currentSongDuration
    }

    /**
     * setter method automatically associates the on seeker bar change listener to be the controller
     * and therefore cannot be null
     * @param seekerBar the seeker bar
     */
    fun init(seekerBar: Slider) {
        this.seekerBar = seekerBar
        this.seekerBar.addOnSliderTouchListener(this)
        this.seekerBar.addOnChangeListener(this)
    }

    override fun logTag(): String {
        return "SKR_MDIA_CNTRLR_CLBK"
    }

    override fun onStartTrackingTouch(slider: Slider) {
        this.isTracking = true
        Log.i(logTag(), "START TRACKING")
        timeCounter.cancelTimerDuringTracking()
        removeValueAnimator()
    }

    override fun onStopTrackingTouch(slider: Slider) {
        this.isTracking = false
        Log.i(logTag(), "Stop TRACKING")
        currentPosition = slider.value
        mediaControllerAdapter.seekTo(currentPosition.toLong())
    }

    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        if (isTracking) { Log.i(logTag(), "PROGRESS CHANGED");
            timeCounter.seekTo(value.toLong())
        }
    }

}