package com.github.goldy1992.mp3player.client.activities

import android.support.v4.media.MediaBrowserCompat
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.views.fragments.PlayToolBarFragment
import com.github.goldy1992.mp3player.client.views.fragments.SimpleTitleBarFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.SongListFragment
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import org.apache.commons.lang3.exception.ExceptionUtils

abstract class FolderActivity : MediaActivityCompat() {

    private var viewPageFragment: MediaItemListFragment? = null
    private var playToolBarFragment: PlayToolBarFragment? = null
    private var simpleTitleBarFragment: SimpleTitleBarFragment? = null
    override val workerId: String
        get() = "FLDER_ACTVY_WKR"

    public override fun initialiseView(layoutId: Int): Boolean {
        setContentView(layoutId)
        simpleTitleBarFragment = supportFragmentManager.findFragmentById(R.id.simpleTitleBarFragment) as SimpleTitleBarFragment?
        playToolBarFragment = supportFragmentManager.findFragmentById(R.id.playToolbarFragment) as PlayToolBarFragment?
        return true
    }

    override fun onConnected() {
        super.onConnected()
        val mediaItem: MediaBrowserCompat.MediaItem = intent.getParcelableExtra(Constants.MEDIA_ITEM)
        val itemLibraryId = MediaItemUtils.getLibraryId(mediaItem)
        viewPageFragment = SongListFragment.newInstance(MediaItemType.FOLDER, itemLibraryId, mediaActivityCompatComponent)
        initialiseView(R.layout.activity_folder)
        supportFragmentManager.beginTransaction().add(R.id.songListFragment, viewPageFragment).commit()
        supportActionBar!!.setTitle(MediaItemUtils.getDirectoryName(mediaItem))
        var titleTextView: TextView? = null
        try {
            val f = simpleTitleBarFragment!!.toolbar.javaClass.getDeclaredField("mTitleTextView")
            f.isAccessible = true
            titleTextView = f[simpleTitleBarFragment!!.toolbar] as TextView
            titleTextView!!.ellipsize = TextUtils.TruncateAt.MARQUEE
            titleTextView.isFocusable = true
            titleTextView.isFocusableInTouchMode = true
            titleTextView.requestFocus()
            titleTextView.setSingleLine(true)
            titleTextView.isSelected = true
            titleTextView.marqueeRepeatLimit = -1
        } catch (ex: NoSuchFieldException) {
            Log.e(LOG_TAG, ExceptionUtils.getMessage(ex))
        } catch (ex: IllegalAccessException) {
            Log.e(LOG_TAG, ExceptionUtils.getMessage(ex))
        }
    }

    override fun onBackPressed() {
        finish()
    }

    companion object {
        private const val LOG_TAG = "FOLDER_ACTIVITY"
    }
}