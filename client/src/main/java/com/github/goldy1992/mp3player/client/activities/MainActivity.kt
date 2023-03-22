package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.media.IMediaBrowser
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.client.ui.rememberWindowSizeClass
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.commons.PermissionsUtils.getAppPermissions
import com.github.goldy1992.mp3player.commons.PermissionsUtils.hasPermission
import com.github.goldy1992.mp3player.commons.data.repositories.permissions.IPermissionsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * The Main Activity
 */
@AndroidEntryPoint(ComponentActivity::class)
open class MainActivity : Hilt_MainActivity(), LogTagger {

    @Inject
    lateinit var componentClassMapper : ComponentClassMapper

    @Inject
    lateinit var permissionsRepository: IPermissionsRepository

    /**
     *
     */
    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    @MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    @DefaultDispatcher
    lateinit var defaultDispatcher: CoroutineDispatcher

    /**
     * PlaybackStateRepo
     */
    @Inject
    lateinit var mediaRepository: MediaRepository

    @Inject
    lateinit var userPreferencesRepository: IUserPreferencesRepository

    @Inject
    lateinit var permissionsNotifier: PermissionsNotifier

    @Inject
    lateinit var mediaBrowser : IMediaBrowser

    var startScreen: Screen = Screen.LIBRARY

    var trackToPlay: Uri? = null
        private set


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(logTag(), "on create called with intent ${intent.action} and data: ${intent.data}")
        super.onCreate(savedInstanceState)

        // If app has already been created set the UI to initialise at the main screen.
        val appAlreadyCreated = savedInstanceState != null
        if (appAlreadyCreated) {
            this.startScreen = Screen.MAIN
        }

        // createService()
        if (Intent.ACTION_VIEW == intent.action) {
            if (intent.data != null) {
                trackToPlay = intent.data
                scope.launch(defaultDispatcher) {
                    mediaRepository.playFromUri(trackToPlay, null)
                }
            }
            this.startScreen = Screen.NOW_PLAYING
        }
       ui()

    }

    override fun onStart() {
        requestPermission(getAppPermissions())
        super.onStart()
    }

    open fun ui() {
        setContent {
            val windowSizeClass = rememberWindowSizeClass()
            ComposeApp(
                userPreferencesRepository = this.userPreferencesRepository,
                windowSize = windowSizeClass,
                startScreen = startScreen
            )

        }
    }

    private fun requestPermission(permissions: Array<String>) { // Here, thisActivity is the current activity
        permissionsRepository.setPermissionsLauncher(permissionLauncher)
        permissionLauncher.launch(permissions)
    }


    private val permissionLauncher : ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
            permissionGrantedArray : Map<String, Boolean> ->

        val allPermissionsGranted = !permissionGrantedArray.containsValue(false)
        if (allPermissionsGranted) {
            permissionsNotifier.setPermissionGranted(true)
        }
        Log.i(logTag(), "permission result: $permissionGrantedArray")
        scope.launch { permissionsRepository.setPermissions(permissionGrantedArray)}
    }

    override fun onDestroy() {
        Log.i(logTag(), "destroying activity")
        mediaBrowser.release()
        this.scope.cancel()
        super.onDestroy()
    }

    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }
}