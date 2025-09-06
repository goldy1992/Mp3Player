package com.github.goldy1992.mp3player.client.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.models.Queue
import com.github.goldy1992.mp3player.client.models.media.Song

private const val LOG_TAG = "ViewPager"
@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ViewPager(
    modifier: Modifier = Modifier,
    currentSongProvider : () -> Song = { Song.DEFAULT},
    queue: Queue = Queue.EMPTY,
    skipToNext : () -> Unit = {},
    skipToPrevious : () -> Unit = {},
    pagerState: PagerState = rememberPagerState(pageCount = { queue.size() }),
) {

    val numberOfPages = queue.size()
    val currentQueuePosition = queue.currentIndex

    if (queue.isEmpty()) {
        Column(modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Empty Playlist", style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center)
        }
    } else {
        LaunchedEffect(currentSongProvider()){
            if (currentQueuePosition < numberOfPages) {
                pagerState.scrollToPage(currentQueuePosition)
            }
        }

        LaunchedEffect(pagerState.currentPage) {
            Log.v(LOG_TAG, "ViewPager() LaunchedEffect: current page changed: ${pagerState.currentPage}")
            val newPosition = pagerState.currentPage
            val atBeginning = currentQueuePosition <= 0
            val atEnd = (currentQueuePosition + 1) >= numberOfPages
            val atCurrentPosition = currentQueuePosition == newPosition

            if (!atCurrentPosition ) {
                if (!atEnd && isSkipToNext(newPosition, currentQueuePosition)) {
                    skipToNext()
                } else if (!atBeginning && isSkipToPrevious(newPosition, currentQueuePosition)) {
                    skipToPrevious()
                }
            }
        }
        Log.v(LOG_TAG, "ViewPager() queue size: $numberOfPages")

        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "viewPagerColumn"
                },
            beyondViewportPageCount = 2,
            key = { page : Int -> queue.items[page].id }

        ) { pageIndex ->
            val item: Song = queue.items[pageIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current).data(item.albumArt).build()
                    ),
                    contentDescription = "Album Art",
                    modifier = Modifier.size(300.dp, 300.dp)
                )
            }
        }

    }

}

private fun isSkipToNext(newPosition: Int, currentPosition : Int): Boolean {
    return newPosition == (currentPosition + 1)
}

private fun isSkipToPrevious(newPosition: Int, currentPosition : Int): Boolean {
    return newPosition == (currentPosition - 1)
}