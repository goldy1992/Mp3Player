package com.example.mike.mp3player.client.views;

import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Description

class TabLayoutMatcher(expectedType : Class<*>)
: ViewMatchers(expectedType) {



    override fun matchesSafely(item : TabLayout) : Boolean {
        return false;
    }

    override fun describeTo(description : Description) {

    }
}
