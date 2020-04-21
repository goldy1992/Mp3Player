package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import androidx.test.espresso.IdlingResource
import androidx.viewpager2.widget.ViewPager2
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.MikesMp3Player
import com.github.goldy1992.mp3player.client.dagger.components.DaggerAndroidTestMediaActivityCompatComponent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityInjectorAndroidTestImpl : MainActivity(), IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun initialiseDependencies() {
        val app : MikesMp3Player = applicationContext!! as MikesMp3Player

        val component: MediaActivityCompatComponent = DaggerAndroidTestMediaActivityCompatComponent
                .factory()
                .create(applicationContext, this, app.getComponentClassMapper())
        mediaActivityCompatComponent = component
        component.inject(this)
    }

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