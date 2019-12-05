package com.github.goldy1992.mp3player.client.views

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.AttributeSetBuilderImpl.ArscResourceResolver

@RunWith(RobolectricTestRunner::class)
class SquareImageViewTest {
    private var context: Context? = null
    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testOnMeasureUsingWidth() {
        val squareImageView = createSquareImageView("X")
        squareImageView.onMeasure(TEST_WIDTH, TEST_HEIGHT)
        Mockito.verify(squareImageView).setDimensions(TEST_WIDTH)
    }

    @Test
    fun testOnMeasureUsingHeight() {
        val squareImageView = createSquareImageView("Y")
        squareImageView.onMeasure(TEST_WIDTH, TEST_HEIGHT)
        Mockito.verify(squareImageView).setDimensions(TEST_HEIGHT)
    }

    private fun createSquareImageView(useWidthOrHeight: String): SquareImageView {
        val resourceResolver = ArscResourceResolver(context)
        val myAttributeSetBuilder = MyAttributeSetBuilderImpl(resourceResolver)
        myAttributeSetBuilder.addAttribute(R.attr.useWidthOrHeight, useWidthOrHeight)
        val attributeSet = myAttributeSetBuilder.build()
        val squareImageView = SquareImageView(context!!, attributeSet, 0)
        val spiedSquareImageView = Mockito.spy(squareImageView)
        Mockito.`when`(spiedSquareImageView.measuredHeight).thenReturn(TEST_HEIGHT)
        Mockito.`when`(spiedSquareImageView.measuredWidth).thenReturn(TEST_WIDTH)
        return spiedSquareImageView
    }

    companion object {
        private const val TEST_WIDTH = 6
        private const val TEST_HEIGHT = 7
    }
}