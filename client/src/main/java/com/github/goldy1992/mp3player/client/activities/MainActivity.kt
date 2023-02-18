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
import androidx.media3.session.MediaBrowser
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.permissions.PermissionGranted
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.client.ui.rememberWindowSizeClass
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.commons.PermissionsUtils.getAppPermissions
import com.github.goldy1992.mp3player.commons.PermissionsUtils.hasPermission
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * The Main Activity
 */
@AndroidEntryPoint(ComponentActivity::class)
open class MainActivity : Hilt_MainActivity(), LogTagger, PermissionsListener {

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
    lateinit var mediaBrowserFuture : ListenableFuture<MediaBrowser>

    var startScreen: Screen = Screen.LIBRARY

    var trackToPlay: Uri? = null
        private set


    companion object {
       private const val REQUEST_CODE = 34



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(logTag(), "on createee")
        super.onCreate(savedInstanceState)
        permissionsNotifier.addListener(this)



        // If app has already been created set the UI to initialise at the main screen.
        val appAlreadyCreated = savedInstanceState != null
        if (appAlreadyCreated) {
            this.startScreen = Screen.MAIN
        }

        requestPermission(getAppPermissions())

    }

    open fun ui() {
        setContent {
            var windowSizeClass = rememberWindowSizeClass()
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
        var allPermissionsAlreadyGranted = permissionsToRequest.isEmpty()
        if (!allPermissionsAlreadyGranted) {
            permissionLauncher.launch(permissions)
        } else { // Permission has already been granted
            Log.i(logTag(), "Permission has already been granted")
            permissionsNotifier.notifyPermissionsGranted()
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
            permissionsNotifier.notifyPermissionsGranted()
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

    private fun createService() {
        Log.i(logTag(), "starting service")
        startService(Intent(applicationContext, componentClassMapper.service))
    }

    override fun onDestroy() {
        Log.i(logTag(), "destroying activity")
        MediaBrowser.releaseFuture(mediaBrowserFuture)
        this.scope.cancel()
        super.onDestroy()
    }

    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }

    override fun onPermissionsGranted() {
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