package com.github.goldy1992.mp3player.service.player

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.service.MyDescriptionAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyPlayerNotificationManagerTest {
    private var myPlayerNotificationManager: MyPlayerNotificationManager? = null
    private var context: Context? = null

    private val myDescriptionAdapter: MyDescriptionAdapter = mock<MyDescriptionAdapter>()

    private val exoPlayer: ExoPlayer = mock<ExoPlayer>()

    private val playerNotificationManager: PlayerNotificationManager = mock<PlayerNotificationManager>()

    private val notificationListener: PlayerNotificationManager.NotificationListener = mock<PlayerNotificationManager.NotificationListener>()

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        myPlayerNotificationManager = MyPlayerNotificationManager(context!!, myDescriptionAdapter, exoPlayer, notificationListener)
        Assert.assertNotNull(myPlayerNotificationManager!!.playbackNotificationManager)
        Assert.assertFalse(myPlayerNotificationManager!!.isActive)
    }

    @Test
    fun testActivate() {
        myPlayerNotificationManager!!.setPlayerNotificationManager(playerNotificationManager)
        myPlayerNotificationManager!!.activate()
        Assert.assertTrue(myPlayerNotificationManager!!.isActive)
        verify(playerNotificationManager, times(1)).setPlayer(exoPlayer)
    }

    @Test
    fun testDeactivate() {
        myPlayerNotificationManager!!.setPlayerNotificationManager(playerNotificationManager)
        myPlayerNotificationManager!!.deactivate()
        Assert.assertFalse(myPlayerNotificationManager!!.isActive)
        verify(playerNotificationManager, times(1)).setPlayer(null)
    }
}