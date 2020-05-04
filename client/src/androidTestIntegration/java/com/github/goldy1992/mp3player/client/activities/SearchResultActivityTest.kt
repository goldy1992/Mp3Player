package com.github.goldy1992.mp3player.client.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchResultActivityTest {

    lateinit var activityScenario: ActivityScenario<SearchResultActivity>

    @Before
    fun setup() {
        this.activityScenario = ActivityScenario.launch(SearchResultActivity::class.java)
    }

    @Test
    fun testSetup() {
        assertTrue(true)
    }
}