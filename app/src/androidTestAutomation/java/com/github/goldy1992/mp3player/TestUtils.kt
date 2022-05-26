package com.github.goldy1992.mp3player

import androidx.compose.ui.test.junit4.ComposeTestRule

object TestUtils {

    private val packageName : String = "com.github.goldy1992.mp3player.automation"
    private val packageSeparator : String = ":"

    fun resourceId(id : String) : String {
        return packageName + packageSeparator + "id/" + id
    }

    fun <T> composeWaitUntilAssertionTrue(composeTestRule : ComposeTestRule,
                             timeout : Long = 25000L,
                             assertion : () -> T) {
        composeTestRule.waitUntil(timeout) {
            var toReturn = false
            composeTestRule.mainClock.autoAdvance = false
            try {
                assertion()
                toReturn = true
            } catch (assertionError: AssertionError) {
                toReturn = false
            }
            finally {
                composeTestRule.mainClock.autoAdvance = true
            }
            toReturn
        }
    }
}