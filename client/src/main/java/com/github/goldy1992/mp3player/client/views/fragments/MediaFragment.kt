package com.github.goldy1992.mp3player.client.views.fragments

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

abstract class MediaFragment : Fragment(), LogTagger, MediaBrowserConnectionListener{
    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter
    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter
    /** Connection Callback */
    @Inject
    lateinit var myConnectionCallback : MyConnectionCallback
    @Inject
    @ActivityContext
    lateinit var activityContext: Context

    lateinit var mainActivity: MainActivity

    protected abstract fun mediaControllerListeners() : Set<Listener>

    /**
     * @return A set of MediaBrowserConnectionListeners to be connected to.
     */
    protected abstract fun mediaBrowserConnectionListeners() : Set<MediaBrowserConnectionListener>


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(logTag(), "logging")
        this.mainActivity = activityContext as MainActivity
        mediaControllerAdapter.registerListeners(mediaControllerListeners())
        myConnectionCallback.registerListeners(mediaBrowserConnectionListeners())
    }

    override fun onConnected() {
        super.onConnected()
    }
}