package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent

class FolderListFragment : MediaItemListFragment() {
    override fun itemSelected(item: MediaBrowserCompat.MediaItem) {
        val intent = intentMapper!!.getIntent(parentItemType)
        if (null != intent) {
            intent.putExtra(Constants.MEDIA_ITEM, item)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(mediaItemType: MediaItemType?, id: String?, component: MediaActivityCompatComponent?): FolderListFragment {
            val folderListFragment = FolderListFragment()
            folderListFragment.init(mediaItemType, id, component!!)
            return folderListFragment
        }
    }
}