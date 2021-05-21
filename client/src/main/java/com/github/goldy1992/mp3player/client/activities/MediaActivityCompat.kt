package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.MediaConnector
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

abstract class MediaActivityCompat : AppCompatActivity(), LogTagger, MediaBrowserConnectionListener {

    /**
     * MediaBrowserAdapter
     */
    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter

    /**
     * Connection Callback
     */
    @Inject
    lateinit var mediaConnector : MediaConnector

    /**
     * MediaControllerAdapter
     */
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
    /**
     *
     */
    @InternalCoroutinesApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val currentThemeResId : Int? = sharedPreferences.getString(Constants.CURRENT_THEME, null)?.toInt()// Int? = currentTheme?.
        if (currentThemeResId != null) {
            setTheme(currentThemeResId)
        }
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