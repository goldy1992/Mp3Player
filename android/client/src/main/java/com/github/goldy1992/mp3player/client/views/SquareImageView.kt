package com.github.goldy1992.mp3player.client.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.AppCompatImageView
import com.github.goldy1992.mp3player.client.R

class SquareImageView
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr) {
    private var useWidthOrHeight = 0

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (useWidthOrHeight) {
            USE_WIDTH -> setDimensions(measuredWidth)
            USE_HEIGHT -> setDimensions(measuredHeight)
            else -> setMeasuredDimension(measuredWidth, measuredHeight)
        }
    }

    @VisibleForTesting
    fun setDimensions(length: Int) {
        setMeasuredDimension(length, length)
    }

    companion object {
        const val USE_WIDTH = 0
        const val USE_HEIGHT = 1
    }

    init {
        if (null != attrs) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView)
            useWidthOrHeight = typedArray.getInteger(R.styleable.SquareImageView_useWidthOrHeight, USE_WIDTH)
        } else {
            useWidthOrHeight = USE_WIDTH
        }
    }
}