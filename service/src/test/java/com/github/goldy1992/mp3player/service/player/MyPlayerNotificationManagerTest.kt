package com.github.goldy1992.mp3player.service.player

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.service.MyDescriptionAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyPlayerNotificationManagerTest {
    private var myPlayerNotificationManager: MyPlayerNotificationManager? = null
    private var context: Context? = null
    @Mock
    private val myDescriptionAdapter: MyDescriptionAdapter? = null
    @Mock
    private val exoPlayer: ExoPlayer? = null
    @Mock
    private val playerNotificationManager: PlayerNotificationManager? = null
    @Mock
    private val notificationListener: PlayerNotificationManager.NotificationListener? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        context = InstrumentationRegistry.getInstrumentation().context
        myPlayerNotificationManager = MyPlayerNotificationManager(context, myDescriptionAdapter!!, exoPlayer!!, notificationListener!!)
        Assert.assertNotNull(myPlayerNotificationManager!!.playbackNotificationManager)
        Assert.assertFalse(myPlayerNotificationManager!!.isActive)
    }

    @Test
    fun testActivate() {
        myPlayerNotificationManager!!.setPlayerNotificationManager(playerNotificationManager)
        myPlayerNotificationManager!!.activate()
        Assert.assertTrue(myPlayerNotificationManager!!.isActive)
        Mockito.verify(playerNotificationManager, Mockito.times(1))!!.setPlayer(exoPlayer)
    }

    @Test
    fun testDeactivate() {
        myPlayerNotificationManager!!.setPlayerNotificationManager(playerNotificationManager)
        myPlayerNotificationManager!!.deactivate()
        Assert.assertFalse(myPlayerNotificationManager!!.isActive)
        Mockito.verify(playerNotificationManager, Mockito.times(1))!!.setPlayer(null)
    }
}