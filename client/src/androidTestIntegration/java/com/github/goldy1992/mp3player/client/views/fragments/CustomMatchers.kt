package com.goldy1992.mp3player.commons

import android.view.View
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object CustomMatchers {
    /**
     * code by cdmunoz: https://gist.github.com/cdmunoz/09db417cca1015dda4f825adefc361ed#file-withseekbarprogress-java
     * @param expectedProgress
     * @return
     */
    fun withSeekbarProgress(expectedProgress: Int): Matcher<View> {
        return CustomBounderMatcher(expectedProgress)
    }

    class CustomBounderMatcher(private val expectedProgress: Int) : BoundedMatcher<View, AppCompatSeekBar>(AppCompatSeekBar::class.java) {
        override fun describeTo(description: Description?) {
            description!!.appendText("expected: ")
            description.appendText("" + expectedProgress)
        }

        override fun matchesSafely(item: AppCompatSeekBar): Boolean {
            return item.progress == expectedProgress
        }
    }
}