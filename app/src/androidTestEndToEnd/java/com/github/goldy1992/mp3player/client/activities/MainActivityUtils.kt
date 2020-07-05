package com.github.goldy1992.mp3player.client.activities

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.TestUtils
import com.github.goldy1992.mp3player.client.AwaitingMediaControllerIdlingResource
import com.github.goldy1992.mp3player.client.views.viewholders.MySongViewHolder

object MainActivityUtils {

    fun togglePlayPauseButton(mDevice: UiDevice) {
        val playPauseButton : String = TestUtils.resourceId("playPauseButton")
        mDevice.findObject(UiSelector().resourceId(playPauseButton)).click()
    }

    fun createAndRegisterIdlingResource() : AwaitingMediaControllerIdlingResource {
        val awaitingMediaControllerIdlingResource = AwaitingMediaControllerIdlingResource()
        IdlingRegistry.getInstance().register(awaitingMediaControllerIdlingResource)
        return awaitingMediaControllerIdlingResource
    }

    fun scrollToRecyclerViewPosition(position : Int) {
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))!!.perform(RecyclerViewActions.scrollToPosition<MySongViewHolder>(position))
    }

    fun clickOnItemWithText(mDevice: UiDevice, text : String) {
        mDevice.findObject(By.text(text)).click()
    }

    fun unregisterIdlingResource(idlingResource: IdlingResource) {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}