package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.MediaConnector
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
    lateinit var mediaConnector : MediaConnector

    /** MediaControllerAdapter  */
    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    public override fun onDestroy() {
        super.onDestroy()
        mediaControllerAdapter.disconnect()
        mediaBrowserAdapter.disconnect()
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
        mediaBrowserAdapter.connect()
    }
}