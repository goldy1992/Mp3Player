package com.github.goldy1992.mp3player.client.activities

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.callbacks.connection.ConnectionStatus
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

        setContent {
            ComposeApp(
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
        connectionStatus.observe(this) {
            mediaBrowserAdapter.onConnectionStatusChanged(it)
            mediaControllerAdapter.onConnectionStatusChanged(it)
        }
        mediaBrowserAdapter.connect()
    }

}