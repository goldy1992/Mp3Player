package com.github.goldy1992.mp3player.client.ui

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
abstract class MediaTestBase {

    protected val testScheduler = TestCoroutineScheduler()
    protected val dispatcher  = StandardTestDispatcher(testScheduler)
    protected val testScope = TestScope(dispatcher)





    val mockNavController : NavController = mock<NavController>()

    open lateinit var context : Context






    val searchResultsState = MutableStateFlow<List<MediaItem>>(emptyList())


    open fun setup() {

    }
}