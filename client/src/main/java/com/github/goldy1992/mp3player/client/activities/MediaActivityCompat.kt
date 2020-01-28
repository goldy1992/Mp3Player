package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.DependencyInitialiser
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

abstract class MediaActivityCompat : AppCompatActivity(), DependencyInitialiser, MediaBrowserConnectorCallback, LogTagger {

    /** MediaBrowserAdapter  */
    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter

    /** MediaControllerAdapter  */
    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    /** @return the mediaActivityCompatComponent */
    lateinit var mediaActivityCompatComponent: MediaActivityCompatComponent

    // MediaBrowserConnectorCallback
    override fun onConnected() {
        mediaControllerAdapter.setMediaToken(mediaBrowserAdapter.mediaSessionToken)
    }

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

    protected abstract fun initialiseView(@LayoutRes layoutId: Int): Boolean
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = applicationContext.getSharedPreferences(Constants.THEME, Context.MODE_PRIVATE)
        setTheme(settings.getInt(Constants.THEME, R.style.AppTheme_Blue))
        mediaBrowserAdapter.connect()
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
}