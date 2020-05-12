package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.CallSuper
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.Constants
import javax.inject.Inject

abstract class MediaActivityCompat : BaseActivity(), MediaInterface  {

    /** MediaBrowserAdapter  */
    @Inject
    override lateinit var mediaBrowserAdapter: MediaBrowserAdapter

    /** Connection Callback */
    @Inject
    override lateinit var myConnectionCallback : MyConnectionCallback

    /** MediaControllerAdapter  */
    @Inject
    override lateinit var mediaControllerAdapter: MediaControllerAdapter

    /** @return the mediaActivityCompatComponent */
    override lateinit var mediaActivityCompatComponent: MediaActivityCompatComponent



    public override fun onDestroy() {
        super.onDestroy()
        disconnect()
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




}