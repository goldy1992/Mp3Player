package com.github.goldy1992.mp3player.client.views.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.commons.LogTagger

abstract class BaseFragment : Fragment(), LogTagger {

    override fun onAttach(context: Context) {
     //   initialiseDependencies()
        super.onAttach(context)
    }
}