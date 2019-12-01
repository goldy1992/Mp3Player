package com.github.goldy1992.mp3player.client.activities

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.PermissionsProcessor
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.dagger.components.DaggerMediaActivityCompatComponent
import com.github.goldy1992.mp3player.service.MediaPlaybackServiceInjector
import org.apache.commons.lang3.exception.ExceptionUtils
import javax.inject.Inject

class SplashScreenEntryActivity : AppCompatActivity(), MediaBrowserConnectorCallback, PermissionGranted {
    private var mediaBrowserAdapter: MediaBrowserAdapter? = null
    private var permissionsProcessor: PermissionsProcessor? = null
    @get:VisibleForTesting
    @set:VisibleForTesting
    @Volatile
    var isSplashScreenFinishedDisplaying = false
    @get:VisibleForTesting
    @set:VisibleForTesting
    @Volatile
    var isPermissionGranted = false
    private var mainActivityIntent: Intent? = null
    @get:VisibleForTesting
    val thread = Thread(Runnable { init() })

    public override fun onCreate(savedInstanceState: Bundle?) {
        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action != null && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }
        initialiseDependencies()
        super.onCreate(savedInstanceState)
        // TODO: have this injected so that a test implementation can be provided
        mainActivityIntent = Intent(applicationContext, mainActivityClass)
        setContentView(R.layout.splash_screen)
        thread.start()
    }

    override fun onStart() {
        super.onStart()
        Log.i(LOG_TAG, "onStart")
    }

    @Synchronized
    private fun splashScreenRun() { //    Log.i(LOG_TAG, "splashscreen run");
        try {
            wait(WAIT_TIME)
        } catch (ex: InterruptedException) {
            Log.e(LOG_TAG, ExceptionUtils.getMessage(ex.fillInStackTrace()))
            Thread.currentThread().interrupt()
        } finally {
            isSplashScreenFinishedDisplaying = true
            notifyAll()
        }
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

    // MediaBrowserConnectorCallback
    override fun onConnected() {
        onProcessingComplete()
        Log.i(LOG_TAG, "hit on connected")
    }

    // MediaBrowserConnectorCallback
    override fun onConnectionSuspended() {}

    // MediaBrowserConnectorCallback
    override fun onConnectionFailed() {
        Log.i(LOG_TAG, "connection failed")
    }

    @Synchronized
    private fun onProcessingComplete() {
        Log.i(LOG_TAG, "processing complete")
        while (!isSplashScreenFinishedDisplaying || !isPermissionGranted) {
            try {
                wait(Constants.ONE_SECOND)
            } catch (ex: InterruptedException) {
                val error = ExceptionUtils.getMessage(ex.fillInStackTrace())
                Log.e(LOG_TAG, error)
                Thread.currentThread().interrupt()
            }
        }
        startMainActivity(mainActivityIntent)
    }

    private fun startMainActivity(mainActivityIntent: Intent?) {
        Log.i(LOG_TAG, "start main activity")
        startActivityForResult(mainActivityIntent, APP_TERMINATED)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_TERMINATED) {
            if (mediaBrowserAdapter != null) {
                mediaBrowserAdapter!!.disconnect()
            }
            finish()
        }
    }

    override fun onPermissionGranted() {
        Log.i(LOG_TAG, "permission granted")
        isPermissionGranted = true
        createService()
        val r: Runnable = Thread(Runnable { mediaBrowserAdapter!!.connect() })
        runOnUiThread(r)
    }

    fun init() {
        Log.i(LOG_TAG, "reset")
        permissionsProcessor!!.requestPermission(permission.WRITE_EXTERNAL_STORAGE)
        val splashScreenWaitThread = Thread(Runnable { splashScreenRun() })
        splashScreenWaitThread.start()
    }

    @Inject
    fun setPermissionsProcessor(permissionsProcessor: PermissionsProcessor?) {
        this.permissionsProcessor = permissionsProcessor
    }

    @Inject
    fun setMediaBrowserAdapter(mediaBrowserAdapter: MediaBrowserAdapter?) {
        this.mediaBrowserAdapter = mediaBrowserAdapter
    }

    private fun createService() {
        startService(Intent(applicationContext, MediaPlaybackServiceInjector::class.java))
    }

    private fun initialiseDependencies() {
        val component = DaggerMediaActivityCompatComponent
                .factory()
                .create(applicationContext, "SPSH_SCRN_ACTVTY_WRKR", this)
        val splashScreenEntryActivityComponent = component.splashScreenEntryActivity().create(this, this)
        splashScreenEntryActivityComponent.inject(this)
    }

    val mainActivityClass: Class<*>
        get() = MainActivityInjector::class.java

    companion object {
        private const val LOG_TAG = "SPLSH_SCRN_ENTRY_ACTVTY"
        private const val WAIT_TIME = 3000L
        const val APP_TERMINATED = 0x78
    }
}