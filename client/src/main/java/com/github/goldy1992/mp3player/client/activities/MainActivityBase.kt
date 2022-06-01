package com.github.goldy1992.mp3player.client.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.client.permissions.PermissionGranted
import com.github.goldy1992.mp3player.client.permissions.PermissionsProcessor
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class MainActivityBase : ComponentActivity(),
    LogTagger,
    PermissionGranted {
    @Inject
    lateinit var componentClassMapper : ComponentClassMapper
    /**
     * MediaBrowserAdapter
     */
    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter

    @Inject
    lateinit var connectionCallback: MyConnectionCallback

    @Inject
    lateinit var permissionsProcessor: PermissionsProcessor
    /**
     * MediaControllerAdapter
     */
    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    lateinit var mediaRepository: MediaRepository

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    var startScreen: Screen = Screen.LIBRARY

    var trackToPlay: Uri? = null
        private set

    abstract fun ui(startScreen : Screen = Screen.SPLASH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    Log.i(logTag(), "on createee")

        // If app has already been created set the UI to initialise at the main screen.
        val appAlreadyCreated = savedInstanceState != null
        if (appAlreadyCreated) {
            this.startScreen = Screen.MAIN
        }

        permissionsProcessor.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionLauncher)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaControllerAdapter.disconnect()
        mediaBrowserAdapter.disconnect()
    }

    private fun connect() {
        connectionCallback.registerListener(mediaControllerAdapter)
        connectionCallback.registerListener(mediaBrowserAdapter)
        mediaBrowserAdapter.connect()
    }

    override fun onPermissionGranted() {
        Log.i(logTag(), "permission granted")
        createService()
        connect()
        if (Intent.ACTION_VIEW == intent.action) {
            trackToPlay = intent.data
            this.lifecycleScope.launch(Dispatchers.Default) {
                mediaControllerAdapter.playFromUri(trackToPlay, null)
            }
            this.startScreen = Screen.NOW_PLAYING
        }
        this.lifecycleScope.launch(Dispatchers.Main) {
            ui(startScreen = startScreen)
        }
    }


    private val permissionLauncher : ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        permissionGranted ->
        run {

            Log.i(logTag(), "permission result: $permissionGranted")
            if (permissionGranted) {
                onPermissionGranted()
            } else {
                this.lifecycleScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.permission_denied),
                        Toast.LENGTH_LONG
                    ).show()
                }
                finish()
            }

        }
    }

    private fun createService() {
        startService(Intent(applicationContext, componentClassMapper.service))
    }
}