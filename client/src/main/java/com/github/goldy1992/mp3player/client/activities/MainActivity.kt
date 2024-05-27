package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.media.IMediaBrowser
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.DefaultDispatcher
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.github.goldy1992.mp3player.commons.PermissionsUtils.getAppPermissions
import com.github.goldy1992.mp3player.commons.Screen
import com.github.goldy1992.mp3player.commons.data.repositories.permissions.IPermissionsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The Main Activity
 */
@AndroidEntryPoint
open class MainActivity : AppCompatActivity(), LogTagger {

    @Inject
    lateinit var componentClassMapper : ComponentClassMapper

    @Inject
    lateinit var permissionsRepository: IPermissionsRepository

    @Inject
    lateinit var sessionToken: SessionToken

    /**
     *
     */
    @ActivityCoroutineScope
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
    lateinit var mediaBrowser : IMediaBrowser

    private var startScreen: Screen = Screen.LIBRARY

    var trackToPlay: Uri? = null
        private set


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(logTag(), "onCreate() invoked with intent ${intent.action} and data: ${intent.data}")
        super.onCreate(savedInstanceState)
        Log.v(logTag(), "onCreate() call to super.onCreate() complete")

        this.mediaBrowser.init(sessionToken, scope)

        // If app has already been created set the UI to initialise at the main screen.
        val appAlreadyCreated = savedInstanceState != null
        if (appAlreadyCreated) {
            Log.d(logTag(), "onCreate() app already created, setting start screen to MAIN")
            this.startScreen = Screen.MAIN
        }

        // createService()
        if (Intent.ACTION_VIEW == intent.action) {
            Log.d(logTag(), "onCreate() intent action is ACTION_VIEW")
            if (intent.data != null) {
                trackToPlay = intent.data
                scope.launch(defaultDispatcher) {
                    Log.v(logTag(), "onCreate() calling MediaRepository.playFromUri() for track: $trackToPlay")
                    mediaRepository.playFromUri(trackToPlay, null)
                    Log.v(logTag(), "onCreate() MediaRepository.playFromUri() for track: $trackToPlay invocation complete.")
                }
            } else {
                Log.w(logTag(), "onCreate() intent with action ACTION_VIEW has NULL data")
            }
            Log.v(logTag(), "onCreate() setting startScreen to be NOW_PLAYING")
            this.startScreen = Screen.NOW_PLAYING
        }
        Log.v(logTag(), "onCreate() calling MainActivity.ui()")
        ui()

    }

    override fun onStart() {
        Log.v(logTag(), "onStart() invoked")
        permissionLauncher.launch(getAppPermissions())
        super.onStart()
    }

    open fun ui() {
        Log.v(logTag(), "ui() invoked")
        setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            windowSizeClass.widthSizeClass
            ComposeApp(
                userPreferencesRepository = this.userPreferencesRepository,
                windowSize = windowSizeClass,
                startScreen = startScreen
            )

        }
        Log.v(logTag(), "ui() invocation complete")
    }

    private val permissionLauncher : ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
            permissionGrantedArray : Map<String, Boolean> ->

        Log.v(logTag(), "permissionLauncher: permission result: $permissionGrantedArray")
        scope.launch { permissionsRepository.setPermissions(permissionGrantedArray)}
    }

    override fun onDestroy() {
        Log.v(logTag(), "onDestroy() invoked")
        mediaBrowser.release()
        Log.v(logTag(), "onDestroy() MediaBrowser released")
        this.scope.cancel()
        Log.v(logTag(), "onDestroy() coroutine scope cancelled")
        super.onDestroy()
        Log.v(logTag(), "onDestroy() super.onDestroy() invocation complete")
    }

    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }
}