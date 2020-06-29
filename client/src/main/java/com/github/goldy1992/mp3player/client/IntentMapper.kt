package com.github.goldy1992.mp3player.client

import android.content.Context
import android.content.Intent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject

@ActivityScoped
class IntentMapper
    @Inject
    constructor(@ApplicationContext private val context: Context,
                private val componentClassMapper: ComponentClassMapper) {
    private val categoryToActivityMap: MutableMap<MediaItemType,
            Class<*>> = EnumMap(MediaItemType::class.java)
    init {
        categoryToActivityMap[MediaItemType.FOLDER] = componentClassMapper.mediaPlayerActivity!!
        categoryToActivityMap[MediaItemType.SONGS] = componentClassMapper.mediaPlayerActivity!!
        categoryToActivityMap[MediaItemType.FOLDERS] = componentClassMapper.folderActivity!!
    }

    fun getIntent(mediaItemType: MediaItemType?): Intent? {
        val clazz = categoryToActivityMap[mediaItemType]
        return clazz?.let { Intent(context, it) }
    }

}