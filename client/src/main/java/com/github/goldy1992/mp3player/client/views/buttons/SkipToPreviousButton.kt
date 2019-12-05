package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import javax.inject.Inject
import javax.inject.Named

class SkipToPreviousButton @Inject constructor(context: Context, mediaControllerAdapter: MediaControllerAdapter, @Named("main") mainUpdater: Handler) : MediaButton(context, mediaControllerAdapter, mainUpdater) {
    override fun init(view: ImageView?) {
        super.init(view)
        setImage(R.drawable.ic_baseline_skip_previous_24px)
    }

    override fun onClick(view: View?) {
        mediaControllerAdapter.skipToPrevious()
    }
}