package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class SkipToPreviousButton @Inject constructor(@ActivityContext context: Context,
                                               mediaControllerAdapter: MediaControllerAdapter)
    : MediaButton(context, mediaControllerAdapter) {

    override fun init(imageView: ImageView) {
        super.init(imageView)
        setImage(R.drawable.ic_baseline_skip_previous_24px)
    }

    override fun onClick(view: View?) {
        mediaControllerAdapter.skipToPrevious()
    }

    override fun logTag(): String {
        return "SKIP_PRV_BTN"
    }
}