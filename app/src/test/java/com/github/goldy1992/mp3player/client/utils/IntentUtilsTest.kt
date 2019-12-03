package com.github.goldy1992.mp3player.client.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.utils.IntentUtils.createGoToMediaPlayerActivity
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IntentUtilsTest {
    @Test
    fun testCreateGoToMediaPlayerActivity() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val result = createGoToMediaPlayerActivity(context)
        Assert.assertNotNull(result)
    }
}