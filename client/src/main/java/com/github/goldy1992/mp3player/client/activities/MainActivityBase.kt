package com.github.goldy1992.mp3player.client.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.permissions.PermissionGranted
import com.github.goldy1992.mp3player.client.permissions.PermissionsProcessor
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferencesRepository
import com.github.goldy1992.mp3player.commons.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class MainActivityBase : ComponentActivity(),
    LogTagger,
    PermissionGranted {
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

    @Inject
    lateinit var permissionsProcessor: PermissionsProcessor
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

    abstract fun ui(startScreen : Screen = Screen.SPLASH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(logTag(), "on createee")

        // If app has already been created set the UI to initialise at the main screen.
        val appAlreadyCreated = savedInstanceState != null
        if (appAlreadyCreated) {
            this.startScreen = Screen.MAIN
        }

        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun requestPermission(permission: String) { // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(permission)
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else { // Permission has already been granted
            Log.i(logTag(), "Permission has already been granted")
            onPermissionGranted()
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
        scope.launch(mainDispatcher) { ui(startScreen = startScreen) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            Log.i(logTag(), "accepted result")
        }
    }


    val permissionLauncher : ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        permissionGranted ->
            Log.i(logTag(), "permission result: $permissionGranted")
            if (permissionGranted) {
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
}