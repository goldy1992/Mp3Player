package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
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
@AndroidEntryPoint(MediaActivityCompat::class)
open class MainActivity : Hilt_MainActivity(),
    CoroutineScope by GlobalScope
{

    private val USER_PREFERENCES_NAME = "user_prefs"

    private val Context.dataStore by preferencesDataStore(
            name = USER_PREFERENCES_NAME,
    )


    @Inject
    lateinit var componentClassMapper: ComponentClassMapper

    lateinit var mediaRepository : MediaRepository

    lateinit var userPreferencesRepository: UserPreferencesRepository

    @get:VisibleForTesting
    var trackToPlay: Uri? = null
        private set


    override fun initialiseView(): Boolean {
        return true
    }

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @InternalCoroutinesApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMediaRepository()
        this.userPreferencesRepository = UserPreferencesRepository(dataStore)

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

}