package com.github.goldy1992.mp3player.client.activities

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.PermissionsProcessor
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.DependencyInitialiser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 *
 */
abstract class SplashScreenEntryActivity : AppCompatActivity(), PermissionGranted, DependencyInitialiser {

    @Inject
    lateinit var componentClassMapper: ComponentClassMapper

    @Inject
    lateinit var permissionsProcessor: PermissionsProcessor

    @Volatile
    var isSplashScreenFinishedDisplaying = false


    @Volatile
    var isPermissionGranted = false
    private var mainActivityIntent: Intent? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action != null && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }
        super.onCreate(savedInstanceState)
        //permissionsProcessor = PermissionsProcessor(this, this)
        // TODO: have this injected so that a test implementation can be provided
        mainActivityIntent = Intent(applicationContext, componentClassMapper.mainActivity)
        CoroutineScope(IO).launch { init()}
    }

    override fun onStart() {
        super.onStart()
        Log.i(LOG_TAG, "onStart")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) { //   Log.i(LOG_TAG, "permission result");
        var permissionIsGranted = false
        if (permissions.size > 0 && grantResults.size > 0) {
            for (i in permissions.indices) {
                if (permission.WRITE_EXTERNAL_STORAGE == permissions[i] &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionIsGranted = true
                }
            }
        }
        if (permissionIsGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(applicationContext, "Permission denied, please enable Storage permissions in settings in order to uer the app", Toast.LENGTH_LONG).show()
            finish()
        }
    }


    private suspend fun onProcessingComplete() {
        Log.i(LOG_TAG, "processing complete")
        while (!(isSplashScreenFinishedDisplaying && isPermissionGranted)) {
           delay(Constants.ONE_SECOND)
        }
        startMainActivity(mainActivityIntent)
    }

    private suspend fun startMainActivity(mainActivityIntent: Intent?) {
        withContext(Main) {
            Log.i(LOG_TAG, "start main activity")
            startActivityForResult(mainActivityIntent, APP_TERMINATED)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_TERMINATED) {
            finish()
        }
    }

    override fun onPermissionGranted() {
        Log.i(LOG_TAG, "permission granted")
        isPermissionGranted = true
        createService()
        CoroutineScope(IO).launch { onProcessingComplete() }
    }

    private suspend fun init() {
        Log.i(LOG_TAG, "reset")
        permissionsProcessor!!.requestPermission(permission.WRITE_EXTERNAL_STORAGE)
        delay(WAIT_TIME)
        isSplashScreenFinishedDisplaying = true
    }

    private fun createService() {
        startService(Intent(applicationContext, componentClassMapper.service))
    }


    companion object {
        private const val LOG_TAG = "SPLSH_SCRN_ENTRY_ACTVTY"
        private const val WAIT_TIME = 3000L
        const val APP_TERMINATED = 0x78
    }
}