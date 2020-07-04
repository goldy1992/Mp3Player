package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

abstract class MediaActivityCompat : AppCompatActivity(), LogTagger, MediaBrowserConnectionListener {

    /** MediaBrowserAdapter  */
    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter

    /** Connection Callback */
    @Inject
    lateinit var myConnectionCallback : MyConnectionCallback

    /** MediaControllerAdapter  */
    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    public override fun onDestroy() {
        super.onDestroy()
        mediaControllerAdapter.disconnect()
        mediaBrowserAdapter.disconnect()
    }

    // MediaBrowserConnectorCallback
    override fun onConnectionSuspended() { /* TODO: implement onConnectionSuspended */
        Log.i(logTag(), "connection suspended")
    }

    // MediaBrowserConnectorCallback
    override fun onConnectionFailed() { /* TODO: implement onConnectionFailed */
        Log.i(logTag(), "connection failed")
    }

    /**
     *
     */
    protected abstract fun initialiseView() : Boolean

    /**
     * @return A set of MediaBrowserConnectionListeners to be connected to.
     */
    protected abstract fun mediaBrowserConnectionListeners() : Set<MediaBrowserConnectionListener>

    /**
     * @return A set of media controller listeners
     */
    protected abstract fun mediaControllerListeners() : Set<Listener>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = applicationContext.getSharedPreferences(Constants.THEME, Context.MODE_PRIVATE)
        setTheme(settings.getInt(Constants.THEME, R.style.AppTheme_Blue))
        connect()
        initialiseView()
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

    private fun connect() {
        myConnectionCallback.registerMediaControllerAdapter(mediaControllerAdapter)
        myConnectionCallback.registerListeners(mediaBrowserConnectionListeners())
        mediaControllerAdapter.registerListeners(mediaControllerListeners())
        mediaBrowserAdapter.connect()
    }
}