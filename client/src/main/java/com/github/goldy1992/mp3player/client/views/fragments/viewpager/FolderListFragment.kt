package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.support.v4.media.MediaBrowserCompat
import androidx.fragment.app.viewModels
import com.github.goldy1992.mp3player.client.viewmodels.MediaListViewModel
import com.github.goldy1992.mp3player.client.viewmodels.SongListViewModel
import com.github.goldy1992.mp3player.client.views.adapters.MyFolderViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecyclerViewAdapter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import javax.inject.Inject

class FolderListFragment : MediaItemListFragment() {

    @Inject
    lateinit var myFolderViewAdapter : MyFolderViewAdapter

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

    private val viewModel : SongListViewModel by viewModels()
    override fun viewModel(): MediaListViewModel {
        return viewModel
    }

    override fun logTag(): String {
        return "FLDR_LST_FRGMNT"
    }

    override fun initialiseDependencies() {
        //createMediaItemListFragmentSubcomponent(this,
          //      arguments?.get(MEDIA_ITEM_TYPE) as MediaItemType,
            //    arguments?.getString(PARENT_ID) as String)
             //   ?.inject(this)
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