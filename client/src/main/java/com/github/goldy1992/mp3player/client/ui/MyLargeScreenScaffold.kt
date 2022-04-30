package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val SMALL_WIDTH : Dp = 72.dp
val LARGE_WIDTH : Dp = 256.dp

@Preview
@Composable
fun myLargeComposable() {
  Surface(color = Color.Blue, modifier = Modifier
   .fillMaxWidth()
   .fillMaxHeight()) {
   Row {
    Surface(color = Color.White, modifier = Modifier.fillMaxHeight().width(SMALL_WIDTH)) {

    }
   }
 }
}