package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.views.adapters.MyFolderViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecycleViewAdapter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import java.util.*
import javax.inject.Inject

class FolderListFragment : MediaItemListFragment() {

    @Inject
    lateinit var myFolderViewAdapter : MyFolderViewAdapter

    override fun mediaControllerListeners(): Set<Listener> {
        return Collections.emptySet()
        TODO("Implement with all the Media Controller Listeners")
    }


    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        val intent = intentMapper.getIntent(parentItemType)
        if (null != intent) {
            intent.putExtra(Constants.MEDIA_ITEM, item)
            startActivity(intent)
        }
    }

    override fun getViewAdapter(): MyGenericRecycleViewAdapter {
        return myFolderViewAdapter
    }

    override fun logTag(): String {
        return "FLDR_LST_FRGMNT"
    }

    override fun initialiseDependencies() {
        createMediaItemListFragmentSubcomponent(this)?.inject(this)
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