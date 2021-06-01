package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.callbacks.connection.ConnectionStatus
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The Main Activity
 */
@AndroidEntryPoint(AppCompatActivity::class)
open class MainActivity : Hilt_MainActivity(),
    LogTagger,
    CoroutineScope by GlobalScope
{
    /**
     * MediaBrowserAdapter
     */
    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter

    @Inject
    lateinit var connectionCallback : MyConnectionCallback

    /**
     * MediaControllerAdapter
     */
    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    @Inject
    lateinit var connectionStatus : MutableLiveData<ConnectionStatus>

    private lateinit var mediaRepository : MediaRepository

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @get:VisibleForTesting
    var trackToPlay: Uri? = null
        private set

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @InternalCoroutinesApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connect()
        initMediaRepository()


        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {

            trackToPlay = intent.data
            launch(Dispatchers.Default) {
                mediaControllerAdapter.playFromUri(trackToPlay, null)
            }
            /* TODO: Add functionallity to navigate to NowPlayingScreen.kt given an intent with an
                     item to play */

        }


        setContent {
            Ui(
                mediaRepository =  mediaRepository,
                mediaBrowserAdapter = mediaBrowserAdapter,
                mediaControllerAdapter = mediaControllerAdapter,
                userPreferencesRepository = userPreferencesRepository)
        }
    }

    private fun initMediaRepository() {
        mediaRepository = MediaRepository(mediaBrowserAdapter.subscribeToRoot())
        mediaRepository.rootItems.observe(this) {
            launch(Dispatchers.Default) {
                for (mediaItem in it) {
                    val id = MediaItemUtils.getMediaId(mediaItem)!!
                    mediaRepository.itemMap[MediaItemUtils.getRootMediaItemType(mediaItem)!!] = mediaBrowserAdapter.subscribe(id)
                }
            }
        }
    }

    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }

    public override fun onDestroy() {
        super.onDestroy()
        mediaControllerAdapter.disconnect()
        mediaBrowserAdapter.disconnect()
    }

    private fun connect() {
        connectionCallback.registerListener(mediaControllerAdapter)
        connectionCallback.registerListener(mediaBrowserAdapter)
        mediaBrowserAdapter.connect()
    }

    @ExperimentalPagerApi
    @OptIn(InternalCoroutinesApi::class)
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    @Composable
    open fun Ui(mediaRepository : MediaRepository,
                    mediaBrowserAdapter : MediaBrowserAdapter,
                    mediaControllerAdapter : MediaControllerAdapter,
                    userPreferencesRepository : UserPreferencesRepository) {
        ComposeApp(
            mediaRepository =  mediaRepository,
            mediaBrowserAdapter = mediaBrowserAdapter,
            mediaControllerAdapter = mediaControllerAdapter,
            userPreferencesRepository = userPreferencesRepository)
    }


}