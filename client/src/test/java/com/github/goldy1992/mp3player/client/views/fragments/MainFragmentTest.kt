package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Looper
import android.view.MenuItem
import androidx.fragment.app.testing.FragmentScenario
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Test
import org.robolectric.Shadows

class MainFragmentTest {

    lateinit var scenario : FragmentScenario<MainFragment>

    @Test
    fun testOnOptionsItemSelectedSearch() {
//        scenario.onFragment { activity: MainFragment ->
//            val menuItem = mock<MenuItem>()
//            // select the search option item
//            activity.onOptionsItemSelected(menuItem)
//            Shadows.shadowOf(Looper.getMainLooper()).idle()
//        }
    }
}