package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.databinding.FragmentMyFirstBinding

class MyFirstFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //super.onCreateView(inflater, container, savedInstanceState)
        return FragmentMyFirstBinding.inflate(inflater).root
        //return inflater.inflate(R.layout.fragment_title_screen, container, false)
        //return inflater.inflate(R.layout.fragment_my_first, container, true)
    }
}