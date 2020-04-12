package com.github.goldy1992.mp3player.client.activities

import androidx.test.espresso.IdlingResource
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityInjectorAndroidTestImpl : MainActivity(), IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return "mainActivity"
    }

    override fun isIdleNow(): Boolean {
        if (this.rootMenuItemsPager != null) {
            val viewPager2: ViewPager2 = this.rootMenuItemsPager
            if (viewPager2.adapter != null) {
                val isIdle: Boolean = viewPager2.adapter!!.itemCount >= 2
                if (isIdle) {
                    resourceCallback!!.onTransitionToIdle()
                }
                return isIdle
            }
        }
        return false
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        resourceCallback = callback
    }
}