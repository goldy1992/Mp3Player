package com.github.goldy1992.mp3player.client.views.fragments

import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@UninstallModules(GlideModule::class,
        MediaBrowserAdapterModule::class,
        MediaControllerAdapterModule::class)
@LooperMode(LooperMode.Mode.PAUSED)
class SearchFragmentTest : FragmentTestBase<SearchFragment>() {

    @Rule
    @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        super.setup(SearchFragment::class.java, false)
        super.addFragmentToActivity()
    }

    @Test
    fun testOnClickOnLayout() {
        val action = FragmentAction { fragment: SearchFragment? -> clickOnLayout(fragment) }
        performAction(action)
    }

    private fun clickOnLayout(fragment: SearchFragment?) {
        fragment!!.onClickOnLayout()
    }
}