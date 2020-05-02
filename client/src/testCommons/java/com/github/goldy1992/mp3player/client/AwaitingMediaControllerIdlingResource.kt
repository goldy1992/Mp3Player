package com.github.goldy1992.mp3player.client

import android.util.Log
import androidx.test.espresso.IdlingResource
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import javax.inject.Inject
import javax.inject.Singleton

@ComponentScope
class AwaitingMediaControllerIdlingResource

@Inject
    constructor(): IdlingResource, LogTagger {

    private var resourceCallback : IdlingResource.ResourceCallback? = null

    private var awaitingPlay = false
    private var awaitingPause = false

    /** {@inheritDoc}  */
    override fun getName(): String {
        return "AwaitingMediaControllerIdlingResource"
    }

    /** {@inheritDoc} */
    override fun isIdleNow(): Boolean {
        val res = !(awaitingPlay || awaitingPause)
        Log.i(logTag(), "isIdleNow : $res")
        return res
    }

    fun isAwaitingPlay() : Boolean {
        return awaitingPlay
    }

    fun waitForPlay() {
        awaitingPlay = true
    }

    fun stopWaitForPlay() {
        awaitingPlay = false
        updateIdleStatus()
    }

    fun isAwaitingPause() : Boolean {
        return awaitingPause
    }


    fun waitForPause() {
        this.awaitingPause = true
    }

    fun stopWaitForPause() {
        this.awaitingPause = false
        updateIdleStatus()
    }

    private fun updateIdleStatus() {
        resourceCallback?.onTransitionToIdle()
    }

    /** {@inheritDoc} */
    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
      }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "waitMedCtrlIdlgRes"
    }
}