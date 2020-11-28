package com.github.goldy1992.mp3player.client

import androidx.annotation.DrawableRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.github.goldy1992.mp3player.ImageMatcher


object PlayPauseButtonAssert {

    fun assertNotPlaying() {
        @DrawableRes
        val playButton = R.drawable.ic_baseline_play_arrow_24px
        val playMatcher : ImageMatcher = ImageMatcher(playButton)
        onView(withId(R.id.playPauseButton)).check(matches(playMatcher))
    }

    fun assertIsPlaying() {
        @DrawableRes
        val pauseButton = R.drawable.ic_baseline_pause_24px
        val pauseMatcher : ImageMatcher = ImageMatcher(pauseButton)
        onView(withId(R.id.playPauseButton)).check(matches(pauseMatcher))
    }
}