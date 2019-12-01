package com.github.goldy1992.mp3player.client.views

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatSeekBar
import com.github.goldy1992.mp3player.R

class SeekerBar
@JvmOverloads constructor(context: Context?,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = R.attr.seekBarStyle)
    : AppCompatSeekBar(context, attrs, defStyleAttr) {

    var valueAnimator: ValueAnimator? = null
    var isTracking = false
    override fun setOnSeekBarChangeListener(listener: OnSeekBarChangeListener) {
        super.setOnSeekBarChangeListener(listener)
    }

    init {
        this.visibility = View.VISIBLE
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }
}