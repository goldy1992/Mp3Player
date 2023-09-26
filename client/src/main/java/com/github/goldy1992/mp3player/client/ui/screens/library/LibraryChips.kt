package com.github.goldy1992.mp3player.client.ui.screens.library

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.Constants


enum class SelectedLibraryItem {
    SONGS,
    FOLDERS,
    ALBUMS,
    NONE
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LibraryChips(
    currentItem : SelectedLibraryItem = SelectedLibraryItem.SONGS,
    onSelected  : (SelectedLibraryItem) -> Unit = {_->}) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (currentItem != SelectedLibraryItem.NONE) {
            IconButton(onClick = { onSelected(SelectedLibraryItem.NONE) }) {
                Icon(Icons.Filled.Close, contentDescription = "remove filter")
            }
            FilterChip(
                onClick = { },
                label = {
                        Text(getChipName(currentItem, LocalContext.current))
                },
                selected = true,
            )
        } else {
            LibraryFilterChip(
                selected = false,
                text = stringResource(id = R.string.songs)
            ) {
                onSelected(SelectedLibraryItem.SONGS)
            }

            LibraryFilterChip(
                selected = false,
                text = stringResource(id = R.string.folders)
            ) {
                onSelected(SelectedLibraryItem.FOLDERS)
            }

            LibraryFilterChip(
                selected = false,
                text = stringResource(id = R.string.albums)
            ) {
                onSelected(SelectedLibraryItem.ALBUMS)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun LibraryFilterChip(
    selected: Boolean = false,
    text: String = Constants.UNKNOWN,
    onSelected: () -> Unit = {}
) {
    FilterChip(
        selected = selected,
        onClick = onSelected,
        label = {
            Text(text)
        }
    )
}

private fun getChipName(selectedLibraryItem: SelectedLibraryItem, context : Context) : String {
    return when(selectedLibraryItem) {
        SelectedLibraryItem.SONGS -> context.getString(R.string.songs)
        SelectedLibraryItem.FOLDERS -> context.getString(R.string.folders)
        SelectedLibraryItem.ALBUMS -> context.getString(R.string.albums)
        else -> Constants.UNKNOWN
    }
}