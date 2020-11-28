package com.github.goldy1992.mp3player.client.views.viewholders

import android.app.Activity
import android.view.View
import androidx.test.core.app.ActivityScenario
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.nhaarman.mockitokotlin2.mock

abstract class MediaItemViewHolderTestBase<V : MediaItemViewHolder> {

    val scenario : ActivityScenario<Activity> = ActivityScenario.launch(Activity::class.java)


    lateinit var albumArtPainter: AlbumArtPainter
    lateinit var mediaItemViewHolder: V

    open fun setup() {
        albumArtPainter = mock<AlbumArtPainter>()
    }
}