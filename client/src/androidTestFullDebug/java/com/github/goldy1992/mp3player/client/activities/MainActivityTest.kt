package com.github.goldy1992.mp3player.client.activities

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    lateinit var scenario : ActivityScenario<MainActivity>

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        this.scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testMain() {
        scenario.onActivity { activity -> {
            activity.onPermissionGranted()
        } }
        assertTrue(true)
    }
}