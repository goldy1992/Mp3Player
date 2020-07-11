package com.github.goldy1992.mp3player.client.views.fragments

//import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaFragmentSubcomponent
import android.content.Context
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

abstract class MediaFragment : Fragment(), LogTagger {

    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter
    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    protected abstract fun mediaControllerListeners() : Set<Listener>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mediaControllerAdapter.registerListeners(mediaControllerListeners())
    }
}