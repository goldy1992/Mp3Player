package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecyclerViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.MySongViewAdapter
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import javax.inject.Inject


class SongListFragment : MediaItemListFragment() {

    @Inject
    lateinit var mySongViewAdapter: MySongViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val toReturn : View? = super.onCreateView(inflater, container, savedInstanceState)
        subscribeUi(mySongViewAdapter, binding)
        return toReturn
    }

    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        mediaControllerAdapter.playFromMediaId(MediaItemUtils.getLibraryId(item), null)
    }

    override fun getViewAdapter(): MyGenericRecyclerViewAdapter {
        return mySongViewAdapter
    }

    override fun logTag(): String {
        return "SONG_LST_FRGMNT"
    }

    override fun initialiseDependencies() {
        createMediaItemListFragmentSubcomponent(this, parentItemTypeId)?.inject(this)
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