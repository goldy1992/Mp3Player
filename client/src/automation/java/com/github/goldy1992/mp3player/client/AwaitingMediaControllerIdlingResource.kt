package com.github.goldy1992.mp3player.client

import androidx.test.espresso.IdlingResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AwaitingMediaControllerIdlingResource

    @Inject
    constructor(): IdlingResource {

    private var resourceCallback : IdlingResource.ResourceCallback? = null

    private var awaitingPlay = false
    private var awaitingPause = false

    /** {@inheritDoc}  */
    override fun getName(): String {
        return "AwaitingMediaControllerIdlingResource"
    }

    /** {@inheritDoc} */
    override fun isIdleNow(): Boolean {
        return !(awaitingPlay || awaitingPause)
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
        if (!isIdleNow) {
            resourceCallback?.onTransitionToIdle()
        }
    }

    /** {@inheritDoc} */
    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
      }
}