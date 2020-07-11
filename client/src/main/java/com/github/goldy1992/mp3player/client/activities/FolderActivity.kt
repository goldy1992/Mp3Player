package com.github.goldy1992.mp3player.client.activities

import android.support.v4.media.MediaBrowserCompat
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.SongListFragment
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_simple_title_bar.*
import org.apache.commons.lang3.exception.ExceptionUtils
import java.util.*
import kotlin.collections.HashSet

@AndroidEntryPoint
class FolderActivity : MediaActivityCompat() {

    private var viewPageFragment: MediaItemListFragment? = null

    override fun initialiseView(): Boolean {
        setContentView(R.layout.activity_folder)
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

    override fun onConnected() {
        super.onConnected()
        val mediaItem: MediaBrowserCompat.MediaItem = intent.getParcelableExtra(Constants.MEDIA_ITEM)
        val itemLibraryId = MediaItemUtils.getLibraryId(mediaItem)!!
        viewPageFragment = SongListFragment.newInstance(MediaItemType.FOLDER, itemLibraryId)
        supportFragmentManager.beginTransaction()
                .add(R.id.songListFragment,
                viewPageFragment as SongListFragment)
                .commit()
        supportActionBar!!.setTitle(MediaItemUtils.getDirectoryName(mediaItem))
        val titleTextView: TextView?
        try {
            val f = titleToolbar!!.javaClass.getDeclaredField("mTitleTextView")
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

    override fun onBackPressed() {
        finish()
    }

    override fun logTag(): String {
        return "FOLDER_ACTIVITY"
    }
}