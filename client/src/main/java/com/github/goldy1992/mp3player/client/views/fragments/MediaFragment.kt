package com.github.goldy1992.mp3player.client.views.fragments

import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

abstract class MediaFragment : Fragment(), LogTagger{
    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter
    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

}