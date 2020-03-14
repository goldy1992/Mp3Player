package com.github.goldy1992.mp3player.client.views

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.R
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.AttributeSetBuilderImpl.ArscResourceResolver

@RunWith(RobolectricTestRunner::class)
class SquareImageViewTest {
    private lateinit var context: Context
    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testOnMeasureUsingWidth() {
        val squareImageView = createSquareImageView("X")
        squareImageView.onMeasure(TEST_WIDTH, TEST_HEIGHT)
        verify(squareImageView).setDimensions(TEST_WIDTH)
    }

    @Test
    fun testOnMeasureUsingHeight() {
        val squareImageView = createSquareImageView("Y")
        squareImageView.onMeasure(TEST_WIDTH, TEST_HEIGHT)
        verify(squareImageView).setDimensions(TEST_HEIGHT)
    }

    private fun createSquareImageView(useWidthOrHeight: String): SquareImageView {
        val resourceResolver = ArscResourceResolver(context)
        val myAttributeSetBuilder = MyAttributeSetBuilderImpl(resourceResolver)
        myAttributeSetBuilder.addAttribute(R.attr.useWidthOrHeight, useWidthOrHeight)
        val attributeSet = myAttributeSetBuilder.build()

        val squareImageView = SquareImageView(context, attributeSet, 0)
        val spiedSquareImageView = spy(squareImageView)
        whenever(spiedSquareImageView.measuredHeight).thenReturn(TEST_HEIGHT)
        whenever(spiedSquareImageView.measuredWidth).thenReturn(TEST_WIDTH)
        return spiedSquareImageView
    }

    companion object {
        private const val TEST_WIDTH = 6
        private const val TEST_HEIGHT = 7
    }
}