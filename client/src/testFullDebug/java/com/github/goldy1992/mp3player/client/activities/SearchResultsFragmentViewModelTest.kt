package com.github.goldy1992.mp3player.client.activities

import android.os.Looper.getMainLooper
import android.support.v4.media.MediaBrowserCompat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class SearchResultsFragmentViewModelTest {

    private val searchResultsFragmentViewModel = SearchResultsFragmentViewModel()

    @Test
    fun testOnSearchResult() {
        val expected = mock<MediaBrowserCompat.MediaItem>()
        searchResultsFragmentViewModel.onSearchResult(listOf(expected))
        shadowOf(getMainLooper()).idle()
        val result = searchResultsFragmentViewModel.searchResults.value!![0]
        assertEquals(expected, result)
    }
}