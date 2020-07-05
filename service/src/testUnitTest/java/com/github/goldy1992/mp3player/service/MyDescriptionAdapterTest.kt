package com.github.goldy1992.mp3player.service

import android.content.Context
import android.media.session.MediaSession
import android.support.v4.media.session.MediaSessionCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.service.dagger.modules.service.ContentManagerModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.MediaSessionCompatModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.SearchDatabaseModule
import com.google.android.exoplayer2.ExoPlayer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject


@RunWith(RobolectricTestRunner::class)
//@UninstallModules(SearchDatabaseModule::class, MediaSessionCompatModule::class, ContentManagerModule::class)
//@Config(application = HiltTestApplication::class)
//@HiltAndroidTest
class MyDescriptionAdapterTest {

    private val playlistManager: PlaylistManager = mock<PlaylistManager>()

    private val player: ExoPlayer = mock<ExoPlayer>()

  //  @Inject @JvmField
    var myDescriptionAdapter: MyDescriptionAdapter? = null

    @Before
    fun setup() {
      //  rule.inject()
        val context = InstrumentationRegistry.getInstrumentation().context
        val token = getMediaSessionCompatToken(context)
        val componentClassMapper = ComponentClassMapper.Builder().build()
        myDescriptionAdapter = MyDescriptionAdapter(context, token, playlistManager, componentClassMapper)
    }

    @Test
    fun testGetCurrentContentTitle() {
        val expectedTitle = "title"
        val testItem = MediaItemBuilder("id")
                .setTitle(expectedTitle)
                .build()
        val index = 7
        whenever(player.currentWindowIndex).thenReturn(index)
        whenever(playlistManager.getItemAtIndex(index)).thenReturn(testItem)
        val result = myDescriptionAdapter!!.getCurrentContentTitle(player)
        Assert.assertEquals(expectedTitle, result)
    }

    private fun getMediaSessionCompatToken(context: Context): MediaSessionCompat.Token {
        val mediaSession = MediaSession(context, "sd")
        val sessionToken = mediaSession.sessionToken
        return MediaSessionCompat.Token.fromToken(sessionToken)
    }
}