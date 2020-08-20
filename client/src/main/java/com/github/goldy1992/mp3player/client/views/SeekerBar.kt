package com.github.goldy1992.mp3player.client.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.github.goldy1992.mp3player.client.R
import com.google.android.material.slider.Slider

class SeekerBar
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = R.attr.seekBarStyle)
    : Slider(context, attrs, defStyleAttr) {

    init {
        this.visibility = View.VISIBLE
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }
}