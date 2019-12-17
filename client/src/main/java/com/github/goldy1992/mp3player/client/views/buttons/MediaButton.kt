package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.Constants

abstract class MediaButton protected constructor(protected val context: Context,
                                                 protected val mediaControllerAdapter: MediaControllerAdapter) {
    protected lateinit var view: ImageView
    open fun init(imageView: ImageView) {
        view = imageView
        view.setOnClickListener(::onClick)
    }

    abstract fun onClick(view: View?)

    protected fun setImage(@DrawableRes drawableRes: Int) {
        this.setImage(drawableRes, Constants.OPAQUE)
    }

    protected fun setImage(@DrawableRes drawableRes: Int, alpha: Int) {
        val drawable = context.getDrawable(drawableRes)
        view.setImageDrawable(drawable)
        view.imageAlpha = alpha
    }

}