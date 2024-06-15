package com.github.goldy1992.mp3player.client.ui.screens.library

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.Constants.UNKNOWN

private const val CLOSE_ICON = "CloseIcon"
private const val LOG_TAG = "ScrollableLibraryChips"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun ScrollableLibraryChips(
    modifier: Modifier = Modifier,

    currentItem : SelectedLibraryItem = SelectedLibraryItem.SONGS,
    onSelected  : (SelectedLibraryItem) -> Unit = {_->}) {
    val visibleItems = mutableListOf<ScrollableItem>()
    val isChipSelected = currentItem != SelectedLibraryItem.NONE
    val closeIconOpacity by animateFloatAsState(if (isChipSelected) 1.0f else 0f,
        label = "closeIconOpacity")
    val closeIcon = ScrollableItem(CLOSE_ICON, closeIconOpacity)
    if (isChipSelected) {
        visibleItems.add(closeIcon)
    }
   // rootItems().childMap
    for (chip in SelectedLibraryItem.values()) {
        if (chip != SelectedLibraryItem.NONE) {
            val libraryChipIsVisible =
                (currentItem == SelectedLibraryItem.NONE) || (currentItem == chip)
            val chipOpacity by animateFloatAsState(targetValue = if (libraryChipIsVisible) 1f else 0f,
                label = "${chip.name}ChipOpacity")
            val chip = ScrollableItem(chip.name, chipOpacity)
            if (libraryChipIsVisible) {
                visibleItems.add(chip)
            }
        }
    }
    val context = LocalContext.current
    LazyRow(
        modifier = modifier) {
        items(visibleItems.size, key = { visibleItems[it].name}) {

            val currentRowItem = visibleItems[it]
            if (CLOSE_ICON == currentRowItem.name) {
                IconButton(modifier = Modifier
                    .padding(5.dp)
                    .alpha(currentRowItem.opacity)
                    .animateItemPlacement(),
                    onClick = {
                        onSelected(SelectedLibraryItem.NONE)
                    }) {
                    Icon(Icons.Filled.Close, tint=MaterialTheme.colorScheme.onSurface, contentDescription = "remove filter")
                }
            } else {
                val e: SelectedLibraryItem = SelectedLibraryItem.valueOf(currentRowItem.name)
                FilterChip(
                    modifier = Modifier
                        .padding(5.dp)
                        .alpha(currentRowItem.opacity)
                        .animateItemPlacement() ,
                    onClick = { onSelected(e) },
                    label = {
                        Text(getChipName(e, context))
                    },
                    selected = currentItem == e,
                )
            }

        }
    }

}

@Preview
@Composable
private fun TestScrollableLibraryChips() {
    var chipState by remember { mutableStateOf(SelectedLibraryItem.NONE) }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(4.dp)) {
        ScrollableLibraryChips(
            modifier = Modifier.fillMaxWidth(),
            currentItem = chipState,
            onSelected = { v -> chipState = v }
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun TestShuffle() {
    var list by remember { mutableStateOf(listOf("A", "B", "C", "D", "E", "F", "G")) }
    LazyColumn {
        item {
            Button(onClick = { list = list.reversed()
            Log.i("", "onCLick")}) {
                Text("Shuffle")
            }
        }
        items(list.size, key = { list[it] }) {
            Text("Item ${list[it]}", Modifier.animateItemPlacement(tween(2000)))
        }
    }
}

private data class ScrollableItem(
    val name: String,
    val opacity: Float
)

private fun getChipName(chip: SelectedLibraryItem, context: Context) : String {
    return when (chip) {
        SelectedLibraryItem.SONGS -> context.getString(R.string.songs)
        SelectedLibraryItem.FOLDERS -> context.getString(R.string.folders)
        SelectedLibraryItem.ALBUMS -> context.getString(R.string.albums)
        else -> UNKNOWN
    }
//    context.getString()
}