package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import javax.inject.Inject

class SkipToNextButton

    @Inject
    constructor(context: Context,
                mediaControllerAdapter: MediaControllerAdapter)
    : MediaButton(context, mediaControllerAdapter) {
    override fun init(imageView: ImageView) {
        super.init(imageView)
        setImage(R.drawable.ic_baseline_skip_next_24px)
    }

    override fun onClick(view: View?) {
        mediaControllerAdapter.skipToNext()
    }

    override fun logTag(): String {
        return "SKIP_NXT_BTN"
    }
}