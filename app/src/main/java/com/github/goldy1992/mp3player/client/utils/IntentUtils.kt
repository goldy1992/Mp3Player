package com.github.goldy1992.mp3player.client.utils

import android.content.Context
import android.content.Intent
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivityInjector

/**
 * Utility classes for making intent objects for other classes and services
 */
object IntentUtils {
    /**
     * Creates an intent to go to the MediaPlayerActivity
     * @param context context
     * @return an Intent to go to the MediaPlayerActivity
     */
    @JvmStatic
    fun createGoToMediaPlayerActivity(context: Context): Intent {
        return Intent(context, MediaPlayerActivityInjector::class.java)
    }
}