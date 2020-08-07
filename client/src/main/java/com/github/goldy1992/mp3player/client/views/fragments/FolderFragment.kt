package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.databinding.FragmentFolderBinding
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.SongListFragment
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.android.material.appbar.MaterialToolbar
import org.apache.commons.lang3.exception.ExceptionUtils

class FolderFragment : DestinationFragment(), LogTagger {

    private lateinit var folderListFragment : MediaItemListFragment

    private lateinit var titleToolbar : MaterialToolbar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentFolderBinding.inflate(layoutInflater)
        val folderItem = arguments?.get("folder") as MediaBrowserCompat.MediaItem
        this.titleToolbar = binding.titleToolbar
        setupToolbar(titleToolbar, MediaItemUtils.getDirectoryName(folderItem)!!)

        folderListFragment = SongListFragment.newInstance(MediaItemType.FOLDER,
                MediaItemUtils.getLibraryId(folderItem)!!)

        this.parentFragmentManager.commit {
            add(R.id.song_list_fragment, folderListFragment as SongListFragment)
        }

        return binding.root
    }


    override fun lockDrawerLayout(): Boolean {
        return false
    }

    private fun setupToolbar(titleToolbar: MaterialToolbar, title : String) {
        titleToolbar.title = title
        val activity : AppCompatActivity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(titleToolbar)
        val supportActionBar = activity.supportActionBar
        supportActionBar!!.hide()
        titleToolbar.setOnClickListener { v : View ->
            if (v == titleToolbar.navigationIcon) {
                        Log.i(logTag(), "pressed")
                    }
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val titleTextView: TextView?
        try {
            val f = titleToolbar.javaClass.superclass!!.getDeclaredField("mTitleTextView")
            f.isAccessible = true
            titleTextView = f[titleToolbar] as TextView
            titleTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
            titleTextView.isFocusable = true
            titleTextView.isFocusableInTouchMode = true
            titleTextView.requestFocus()
            titleTextView.setSingleLine(true)
            titleTextView.isSelected = true
            titleTextView.marqueeRepeatLimit = -1
        } catch (ex: NoSuchFieldException) {
            Log.e(logTag(), ExceptionUtils.getMessage(ex))
        } catch (ex: IllegalAccessException) {
            Log.e(logTag(), ExceptionUtils.getMessage(ex))
        }
    }

    override fun logTag(): String {
        return "FOLDER_FRAGMENT"
    }
}