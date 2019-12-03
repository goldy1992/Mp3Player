package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.os.Bundle
import android.os.HandlerThread
import android.util.Log
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent
import javax.inject.Inject

abstract class MediaActivityCompat : AppCompatActivity(), MediaBrowserConnectorCallback {

    /** MediaBrowserAdapter  */
    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter


    /** MediaControllerAdapter  */
    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter


    /** @return the mediaActivityCompatComponent */
    lateinit var mediaActivityCompatComponent: MediaActivityCompatComponent

    /** Thread used to deal with none UI tasks  */
    @Inject
    lateinit var worker: HandlerThread

    /** @return The unique name of the HandlerThread used by the activity
     */
    abstract val workerId: String

    /** Utility method used to initialise the dependencies set up by Dagger2. DOES NOT need to be
     * called by sub class  */
    protected abstract fun initialiseDependencies()

    // MediaBrowserConnectorCallback
    override fun onConnected() {
        mediaControllerAdapter!!.setMediaToken(mediaBrowserAdapter!!.mediaSessionToken)
    }

    public override fun onDestroy() {
        super.onDestroy()
        mediaControllerAdapter!!.disconnect()
        mediaBrowserAdapter!!.disconnect()
        worker!!.quitSafely()
    }

    // MediaBrowserConnectorCallback
    override fun onConnectionSuspended() { /* TODO: implement onConnectionSuspended */
        Log.i(LOG_TAG, "connection suspended")
    }

    // MediaBrowserConnectorCallback
    override fun onConnectionFailed() { /* TODO: implement onConnectionFailed */
        Log.i(LOG_TAG, "connection failed")
    }

    protected abstract fun initialiseView(@LayoutRes layoutId: Int): Boolean
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = applicationContext.getSharedPreferences(Constants.THEME, Context.MODE_PRIVATE)
        setTheme(settings.getInt(Constants.THEME, R.style.AppTheme_Blue))
        mediaBrowserAdapter!!.init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val LOG_TAG = "MEDIA_ACTIVITY_COMPAT"
    }
}