package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils


class SongListFragment : MediaItemListFragment() {

    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        mediaControllerAdapter!!.playFromMediaId(MediaItemUtils.getLibraryId(item), null)
    }

    companion object {
        @JvmStatic
        fun newInstance(mediaItemType: MediaItemType?, id: String?, component: MediaActivityCompatComponent?): SongListFragment {
            val songListFragment = SongListFragment()
            songListFragment.init(mediaItemType, id, component!!)
            return songListFragment
        }
    }
}