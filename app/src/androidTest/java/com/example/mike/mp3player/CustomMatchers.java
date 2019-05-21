package com.example.mike.mp3player;

import android.view.View;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;


public class CustomMatchers {

    /**
     * code by cdmunoz: https://gist.github.com/cdmunoz/09db417cca1015dda4f825adefc361ed#file-withseekbarprogress-java
     * @param expectedProgress
     * @return
     */
    public static Matcher<View> withSeekbarProgress(final int expectedProgress) {
        return new BoundedMatcher<View, AppCompatSeekBar>(AppCompatSeekBar.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("expected: ");
                description.appendText("" + expectedProgress);
            }

            @Override
            public boolean matchesSafely(AppCompatSeekBar seekBar) {
                return seekBar.getProgress() == expectedProgress;
            }
        };
    }
}
