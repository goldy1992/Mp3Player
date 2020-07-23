package com.github.goldy1992.mp3player.client.activities

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.commit
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.SongListFragment
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import org.apache.commons.lang3.exception.ExceptionUtils
import java.util.*
import kotlin.collections.HashSet

@AndroidEntryPoint
class FolderActivity : MediaActivityCompat() {

    private var viewPageFragment: MediaItemListFragment? = null

    override fun initialiseView(): Boolean {
        setContentView(R.layout.activity_folder)
        val folderItem : MediaItem = intent.getParcelableExtra(Constants.MEDIA_ITEM)
        val titleToolbar : MaterialToolbar = findViewById(R.id.titleToolbar)
        setupToolbar(titleToolbar, MediaItemUtils.getDirectoryName(folderItem)!!)
        viewPageFragment = SongListFragment.newInstance(MediaItemType.FOLDER,
                MediaItemUtils.getLibraryId(folderItem)!!)

        supportFragmentManager.commit {
            add(R.id.song_list_fragment, viewPageFragment as SongListFragment)
        }
        return true
    }

    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        val toReturn : MutableSet<MediaBrowserConnectionListener> = HashSet()
        toReturn.add(this)
        return toReturn
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return Collections.emptySet()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun logTag(): String {
        return "FOLDER_ACTIVITY"
    }

    private fun setupToolbar(titleToolbar: MaterialToolbar, title : String) {
        titleToolbar.title = title
        setSupportActionBar(titleToolbar)
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
}