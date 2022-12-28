package com.github.goldy1992.mp3player.client.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.permissions.PermissionGranted
import com.github.goldy1992.mp3player.client.permissions.PermissionsProcessor
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.client.ui.rememberWindowSizeClass
import com.github.goldy1992.mp3player.commons.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The Main Activity
 */
@AndroidEntryPoint(ComponentActivity::class)
open class MainActivity : Hilt_MainActivity(), LogTagger, PermissionGranted {

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

    var startScreen: Screen = Screen.LIBRARY

    var trackToPlay: Uri? = null
        private set


    companion object {
       private const val REQUEST_CODE = 34

        @RequiresApi(TIRAMISU)
        private val TIRAMISU_PERMISSIONS =  arrayOf(Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES)

        private val STANDARD_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(logTag(), "on createee")

        // If app has already been created set the UI to initialise at the main screen.
        val appAlreadyCreated = savedInstanceState != null
        if (appAlreadyCreated) {
            this.startScreen = Screen.MAIN
        }

        requestPermission(calculatePermissions())

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
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }
        var allPermissionsAlreadyGranted = permissionsToRequest.isEmpty()
        if (!allPermissionsAlreadyGranted) {
            permissionLauncher.launch(permissions)
        } else { // Permission has already been granted
            Log.i(logTag(), "Permission has already been granted")
            onPermissionGranted()
        }
    }

    private fun calculatePermissions() : Array<String> {
        return if (Build.VERSION.SDK_INT >= TIRAMISU) {
            TIRAMISU_PERMISSIONS
        } else {
            STANDARD_PERMISSIONS
        }
    }


    override fun onPermissionGranted() {
        Log.i(logTag(), "permission granted")
        createService()
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
            onPermissionGranted()
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
        startService(Intent(applicationContext, componentClassMapper.service))
    }

    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }
}