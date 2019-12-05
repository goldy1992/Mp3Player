package com.github.goldy1992.mp3player.client

import android.content.Context
import android.content.Intent
import com.github.goldy1992.mp3player.client.activities.FolderActivityInjector
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivityInjector
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope
import java.util.*
import javax.inject.Inject

@ComponentScope
class IntentMapper @Inject constructor(private val context: Context) {
    private val categoryToActivityMap: MutableMap<MediaItemType, Class<out MediaActivityCompat?>> = EnumMap(MediaItemType::class.java)
    private fun init() {
        categoryToActivityMap[MediaItemType.FOLDER] = MediaPlayerActivityInjector::class.java
        categoryToActivityMap[MediaItemType.SONGS] = MediaPlayerActivityInjector::class.java
        categoryToActivityMap[MediaItemType.FOLDERS] = FolderActivityInjector::class.java
    }

    fun getIntent(mediaItemType: MediaItemType?): Intent? {
        val clazz = categoryToActivityMap[mediaItemType]
        return clazz?.let { Intent(context, it) }
    }

    init {
        init()
    }
}