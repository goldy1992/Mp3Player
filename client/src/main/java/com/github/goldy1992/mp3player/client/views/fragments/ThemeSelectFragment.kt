package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.goldy1992.mp3player.client.databinding.FragmentThemeSelectBinding

class ThemeSelectFragment : DestinationFragment() {

    override fun lockDrawerLayout(): Boolean {
       return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentThemeSelectBinding.inflate(inflater)
        return binding.root
    }
}