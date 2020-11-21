package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.support.v4.media.MediaBrowserCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.goldy1992.mp3player.client.viewmodels.FolderListViewModel
import com.github.goldy1992.mp3player.client.viewmodels.MediaListViewModel
import com.github.goldy1992.mp3player.client.views.adapters.MyFolderViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecyclerViewAdapter
import com.github.goldy1992.mp3player.client.views.fragments.MainFragmentDirections
import com.github.goldy1992.mp3player.commons.MediaItemType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint(MediaItemListFragment::class)
class FolderListFragment : Hilt_FolderListFragment() {

    @Inject
    lateinit var myFolderViewAdapter : MyFolderViewAdapter

    private val viewModel : FolderListViewModel by viewModels()

    override fun viewModel(): MediaListViewModel {
        return viewModel
    }

    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        val action = MainFragmentDirections.goToFolderFragment(item)
        findNavController().navigate(action)
    }

    override fun getViewAdapter(): MyGenericRecyclerViewAdapter {
        return myFolderViewAdapter
    }


    override fun logTag(): String {
        return "FLDR_LST_FRGMNT"
    }

    companion object {
        @JvmStatic
        fun newInstance(mediaItemType: MediaItemType, id: String): FolderListFragment {
            val folderListFragment = FolderListFragment()
            val args = createArguments(mediaItemType, id)
            folderListFragment.arguments = args
            return folderListFragment
        }
    }
}