package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview

enum class FilterChips {
    SONG,
    FOLDERS,
    ALBUMS,
    ARTISTS,
    NONE
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Preview
@Composable
fun FilterChips(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    Column(modifier= modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        var currentSelection : FilterChips by remember {mutableStateOf(FilterChips.NONE) }

        Row {
            if (currentSelection == FilterChips.NONE) {
                for (c in FilterChips.values()) {
                    if (c != FilterChips.NONE) {
                        FilterChip(selected = false, onClick = { currentSelection = c }) {
                            Text(c.name)
                        }
                    }
                }
            } else {
                FilterChip(selected = false,
                    onClick = { currentSelection = FilterChips.NONE }) {
                    Icon(Icons.Default.Clear, contentDescription = "close")
                }
                FilterChip(selected = true, onClick = { /*TODO*/ }) {
                    Text(text = currentSelection!!.name)
                }
            }
        }

    }
}
