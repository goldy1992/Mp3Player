package com.github.goldy1992.mp3player.client.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val SMALL_WIDTH: Dp = 72.dp
val LARGE_WIDTH: Dp = 256.dp

enum class MenuState { Expanded, Collapsed }

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun LargeNavigationMenu() {
    var expanded by remember { mutableStateOf(MenuState.Collapsed) }
    val width: Dp by animateDpAsState(targetValue = if (expanded == MenuState.Collapsed) SMALL_WIDTH else LARGE_WIDTH)

    LargeMenu(width = width) {
        expanded = if (expanded == MenuState.Collapsed) {
            MenuState.Expanded
        } else {
            MenuState.Collapsed
        }
    }


}


@Composable
fun LargeMenu(width : Dp,
    onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Row {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width = width)
            ) {
                IconButton(onClick = {
                    onClick()
                }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }

            }
        }
    }
}