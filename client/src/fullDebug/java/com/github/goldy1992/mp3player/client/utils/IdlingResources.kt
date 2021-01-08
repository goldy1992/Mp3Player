package com.github.goldy1992.mp3player.client.utils

import androidx.test.espresso.idling.CountingIdlingResource

class IdlingResources {

    companion object {

        val idlingResource : CountingIdlingResource = CountingIdlingResource("SearchResultsFragment_IdlingResource", true)
    }
}