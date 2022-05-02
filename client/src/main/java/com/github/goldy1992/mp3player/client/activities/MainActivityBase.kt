package com.github.goldy1992.mp3player.client.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.*
import javax.inject.Inject

abstract class MainActivityBase : ComponentActivity(),
    LogTagger,
    CoroutineScope by GlobalScope,
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
    Log.i(logTag(), "on createee")

        val intent = intent
//        if (!isTaskRoot
//            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
//            && intent.action != null && intent.action == Intent.ACTION_MAIN) {
//            finish()
//            return
//        }

        // If app has already been created set the UI to initialise at the main screen.
        val appAlreadyCreated = savedInstanceState != null
        if (appAlreadyCreated) {
            this.startScreen = Screen.MAIN
        }

        permissionsProcessor.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

    @OptIn(ExperimentalPagerApi::class, InternalCoroutinesApi::class,
        ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class
    )
    override fun onPermissionGranted() {
        Log.i(logTag(), "permission granted")
        createService()
        connect()
        //initMediaRepository()
        if (Intent.ACTION_VIEW == intent.action) {
            trackToPlay = intent.data
            launch(Dispatchers.Default) {
                mediaControllerAdapter.playFromUri(trackToPlay, null)
            }
            this.startScreen = Screen.NOW_PLAYING
        } 
        CoroutineScope(Dispatchers.Main).launch { ui(startScreen = startScreen) }


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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(logTag(), "permission result");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionIsGranted = false
        if (permissions.isNotEmpty() && grantResults.isNotEmpty()) {
            permissions.forEach {
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE == it && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    permissionIsGranted = true
                }
            }
        }
        Log.i(logTag(), "permission result: $permissionIsGranted")
        if (permissionIsGranted) {
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