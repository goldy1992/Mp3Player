package com.github.goldy1992.mp3player.client.views.fragments

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.github.goldy1992.mp3player.R

object SearchFragmentUtils {

    /**
     * assumes search fragment is open
     */
    fun performSearchQuery(query : String) {
        Espresso.onView(ViewMatchers.withId(R.id.search_src_text)).perform(ViewActions.typeText("prim"))

//        com.github.goldy1992.mp3player.automation:id/search_go_btn
        Espresso.onView(ViewMatchers.withId(R.id.search_go_btn)).perform(ViewActions.click())
    }

    fun openSearchFragment() {
        Espresso.onView(ViewMatchers.withId(R.id.action_search)).perform(ViewActions.click())
    }

    fun closeSearchFragment() {
        // TODO: implement when needed
    }
}