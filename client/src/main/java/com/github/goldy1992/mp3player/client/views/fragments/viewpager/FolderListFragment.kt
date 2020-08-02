package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.support.v4.media.MediaBrowserCompat
import androidx.fragment.app.viewModels
import com.github.goldy1992.mp3player.client.IntentMapper
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.viewmodels.FolderListViewModel
import com.github.goldy1992.mp3player.client.viewmodels.MediaListViewModel
import com.github.goldy1992.mp3player.client.views.adapters.MyFolderViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecyclerViewAdapter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FolderListFragment : MediaItemListFragment() {

    @Inject
    lateinit var myFolderViewAdapter : MyFolderViewAdapter

    @Inject
    lateinit var intentMapper : IntentMapper

    private val viewModel : FolderListViewModel by viewModels()

    override fun viewModel(): MediaListViewModel {
        return viewModel
    }

    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        val parentItemType : MediaItemType = requireArguments().get(MEDIA_ITEM_TYPE) as MediaItemType
        val intent = intentMapper.getIntent(parentItemType)
        if (null != intent) {
            intent.putExtra(Constants.MEDIA_ITEM, item)
            startActivity(intent)
        }
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