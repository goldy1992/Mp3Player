@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.github.goldy1992.mp3player.client.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

val DEFAULT_PADDING = Dp(5f)
val DEFAULT_WINDOW_CLASS_SIZE : WindowSizeClass = WindowSizeClass.calculateFromSize(DpSize(500.dp, 800.dp))
val DEFAULT_NUMBER_OF_TABS = 3
