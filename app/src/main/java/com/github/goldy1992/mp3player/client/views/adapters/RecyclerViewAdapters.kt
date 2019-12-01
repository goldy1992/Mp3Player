package com.github.goldy1992.mp3player.client.views.adapters

import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope
import javax.inject.Inject

@ComponentScope
class RecyclerViewAdapters @Inject constructor(private val mySongViewAdapter: MySongViewAdapter,
                                               private val myFolderViewAdapter: MyFolderViewAdapter) {
    fun getAdapter(mediaItemType: MediaItemType?): MyGenericRecycleViewAdapter? {
        return when (mediaItemType) {
            MediaItemType.SONGS, MediaItemType.FOLDER -> mySongViewAdapter
            MediaItemType.FOLDERS -> myFolderViewAdapter
            else -> null
        }
    }

}