package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.views.adapters.MyFolderViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecyclerViewAdapter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import javax.inject.Inject

class FolderListFragment : MediaItemListFragment() {

    @Inject
    lateinit var myFolderViewAdapter : MyFolderViewAdapter

    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
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

    override fun initialiseDependencies() {
        createMediaItemListFragmentSubcomponent(this, this.parentItemTypeId)?.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(mediaItemType: MediaItemType, id: String): FolderListFragment {
            val folderListFragment = FolderListFragment()
            folderListFragment.init(mediaItemType, id)
            return folderListFragment
        }
    }
}