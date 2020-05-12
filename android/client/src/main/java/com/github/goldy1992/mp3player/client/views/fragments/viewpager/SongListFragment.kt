package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecycleViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.MySongViewAdapter
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import javax.inject.Inject


class SongListFragment : MediaItemListFragment() {

    @Inject
    lateinit var mySongViewAdapter: MySongViewAdapter;

    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        mediaControllerAdapter.playFromMediaId(MediaItemUtils.getLibraryId(item), null)
    }

    override fun getViewAdapter(): MyGenericRecycleViewAdapter {
        return mySongViewAdapter
    }

    override fun logTag(): String {
        return "SONG_LST_FRGMNT"
    }

    override fun initialiseDependencies() {
        createMediaItemListFragmentSubcomponent(this)?.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(mediaItemType: MediaItemType, id: String): SongListFragment {
            val songListFragment = SongListFragment()
            songListFragment.init(mediaItemType, id)
            return songListFragment
        }
    }
}