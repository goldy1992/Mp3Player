package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.media.IMediaBrowser
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.client.ui.rememberWindowSizeClass
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.commons.PermissionsUtils.getAppPermissions
import com.github.goldy1992.mp3player.commons.PermissionsUtils.hasPermission
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

        requestPermission(getAppPermissions())

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
        val permissionsToRequest = mutableSetOf<String>()
        for (permission in permissions) {
            if (!hasPermission(permission, this)) {
                permissionsToRequest.add(permission)
            }
        }
        val allPermissionsAlreadyGranted = permissionsToRequest.isEmpty()
        if (!allPermissionsAlreadyGranted) {
            permissionLauncher.launch(permissions)
        } else { // Permission has already been granted
            Log.i(logTag(), "Permission has already been granted")
            permissionsNotifier.setPermissionGranted(true)
            onPermissionsGranted()
        }
    }


    private val permissionLauncher : ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
            permissionGrantedArray : Map<String, Boolean> ->
        Log.i(logTag(), "permission result: $permissionGrantedArray")
        val rejectedPermissionsSet = mutableSetOf<String>()
        for (permission in permissionGrantedArray.entries) {
            if (!permission.value) {
                rejectedPermissionsSet.add(permission.key)
            }
        }
        val allPermissionsGranted = rejectedPermissionsSet.isEmpty()
        if (allPermissionsGranted) {
            permissionsNotifier.setPermissionGranted(true)
            onPermissionsGranted()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
            finish()
        }
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

    fun onPermissionsGranted() {
        Log.i(logTag(), "permission granted")
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
        scope.launch(mainDispatcher) { ui() }
    }
}