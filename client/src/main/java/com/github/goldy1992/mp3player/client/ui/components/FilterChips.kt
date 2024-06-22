package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

enum class FilterChips {
    SONG,
    FOLDERS,
    ALBUMS,
    ARTISTS,
    NONE
}

@Preview
@Composable
fun FilterChips(modifier: Modifier = Modifier) {
    Column(
        modifier= modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        var currentSelection : FilterChips by remember {mutableStateOf(FilterChips.NONE) }

        Row {
            if (currentSelection == FilterChips.NONE) {
                for (c in FilterChips.entries) {
                    if (c != FilterChips.NONE) {
                        FilterChip(selected = false, onClick = { currentSelection = c }, label = {
                            Text(c.name)
                        }
                        )
                    }
                }
            } else {
                FilterChip(
                    selected = false,
                    onClick = {
                        currentSelection = FilterChips.NONE
                    },
                    label = {
                        Icon(Icons.Default.Clear, contentDescription = "close")
                    }
                )

                FilterChip(
                    selected = true,
                    onClick = { /*TODO*/ },
                    label = {
                        Text(text = currentSelection.name)
                    }
                )
            }
        }
    }
}

