package com.github.goldy1992.mp3player.client.views

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.TextView
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.client.utils.TimerUtils.calculateCurrentPlaybackPosition
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.Constants.ONE_SECOND
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlin.concurrent.timer
import java.util.*

import javax.inject.Inject

@FragmentScope
class TimeCounter

    @Inject
    constructor(private val mediaControllerAdapter: MediaControllerAdapter) : LogTagger {
    private var textView: TextView? = null

    var duration: Long = 0L

    var currentPosition: Long = 0
        private set
    var currentState = PlaybackStateCompat.STATE_NONE
        private set
    var currentSpeed = 0f
        private set

    var timer: Timer? = null

    var isRepeating = false
    private val isInitialised: Boolean
        get() = textView != null

    fun seekTo(position: Long) {
        currentPosition = position
        updateTimerText()
    }

    fun cancelTimerDuringTracking() { //Log.d(LOG_TAG, "cancel timer during tracking");
        cancelTimer()
    }

    fun updateState(state: PlaybackStateCompat) { //Log.d(LOG_TAG, "new state");
        currentState = state.state
        currentSpeed = state.playbackSpeed
        val repeatMode = mediaControllerAdapter.getRepeatMode()
        isRepeating = repeatMode != null && repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE
        val latestPosition = calculateCurrentPlaybackPosition(state)
        if (isInitialised) {
            when (currentState) {
                PlaybackStateCompat.STATE_PLAYING -> work(latestPosition)
                PlaybackStateCompat.STATE_PAUSED -> haltTimer(latestPosition)
                else -> resetTimer()
            }
        }
    }

    private fun work(startTime: Long) { //Log.d(LOG_TAG, "work timer");
        currentPosition = startTime
        updateTimerText()
        createTimer()
    }

    private fun haltTimer(currentTime: Long) {
        Log.i(logTag(), "halt timer")
        timer?.cancel()
        currentPosition = currentTime
        updateTimerText()
    }

    private fun resetTimer() { //Log.d(LOG_TAG, "reset timer");
        timer?.cancel()
        currentPosition = START
        updateTimerText()
    }

    private fun cancelTimer() { //Log.d(LOG_TAG, "Cancel timer");

    }

    private fun createTimer() {
        timer?.cancel()
        timer = timer("", false, 0L, timerFixedRate) {
            CoroutineScope(Main).launch {
                updateUi()
            }
        }
        //Log.d(LOG_TAG, "create timer");
    }

    /**
     * e.g. slower playback speed => longer update time
     * 0.95 playbacks speed => 1000ms / 0.95 = 1052
     */
    private val timerFixedRate: Long
        get() =
                /**
                 * e.g. slower playback speed => longer update time
                 * 0.95 playbacks speed => 1000ms / 0.95 = 1052
                 */
            (ONE_SECOND / currentSpeed).toLong()

    private fun updateTimerText() {
        val text = formatTime(currentPosition)
        textView!!.text = text
    }

    private fun updateUi() { //Log.d(LOG_TAG,"current position: " + timeCounter.getCurrentPosition() + ", duration: " + timeCounter.getDuration());
        val newPosition: Long = currentPosition + ONE_SECOND
        if (newPosition < duration) {
            currentPosition = newPosition
            updateTimerText()
        } else if (isRepeating) {
            currentPosition = Constants.DEFAULT_POSITION.toLong()
            updateTimerText()
        }
        //Log.d(LOG_TAG, "finished run call");
    } // run

    val isRunning: Boolean
        get() = currentState == PlaybackStateCompat.STATE_PLAYING

    fun init(textView: TextView?) {
        this.textView = textView
    }

    companion object {
        private const val START = 0L
    }

    override fun logTag(): String {
        return "TimeCounter";
    }
}