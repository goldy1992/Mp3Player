package com.github.goldy1992.mp3player.client.ui.screens.library

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.utils.MediaItemNameUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val LOG_TAG = "LibraryTabs"
@ExperimentalFoundationApi
@Composable
fun LibraryTabs(
    pagerState: PagerState,
    rootChildrenProvider:  () -> Root,
    scope: CoroutineScope
) {

    val rootItemsState = rootChildrenProvider()

    if (rootItemsState.state == State.LOADED) {
        val rootItems = rootItemsState.childMap
        Column {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                rootItems.keys.forEachIndexed { index, key ->
                    val item = rootItems[key]
                    if (item != null) {
                        val isSelected = index == pagerState.currentPage
                        val context = LocalContext.current
                        Tab(
                            selected = isSelected,
                            modifier = Modifier
                                .height(48.dp)
                                .padding(start = 10.dp, end = 10.dp),
                            content = {
                                Text(
                                    text = MediaItemNameUtils.getMediaItemTypeName(
                                        context,
                                        item.type
                                    )
                                        .uppercase(),//getRootMediaItemType(item = item)?.name ?: Constants.UNKNOWN,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                )
                            },
                            onClick = {                    // Animate to the selected page when clicked
                                scope.launch {
                                    Log.d(
                                        LOG_TAG,
                                        "Clicked to go to index ${index}, string: ${item.id} "
                                    )
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}