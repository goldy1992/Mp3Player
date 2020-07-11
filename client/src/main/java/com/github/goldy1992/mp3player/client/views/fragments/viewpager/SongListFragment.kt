package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.fragment.app.viewModels
import com.github.goldy1992.mp3player.client.viewmodels.MediaListViewModel
import com.github.goldy1992.mp3player.client.viewmodels.SongListViewModel
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecyclerViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.MySongViewAdapter
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SongListFragment : MediaItemListFragment() {

    @Inject
    lateinit var mySongViewAdapter: MySongViewAdapter

    private val viewModel : SongListViewModel by viewModels()

    override fun viewModel(): MediaListViewModel {
        return viewModel
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

    companion object {
        @JvmStatic
        fun newInstance(mediaItemType: MediaItemType, id: String): SongListFragment {
            val songListFragment = SongListFragment()
            val args : Bundle = createArguments(mediaItemType, id)
            songListFragment.arguments = args
            return songListFragment
        }
    }
}