package com.github.goldy1992.mp3player.client.ui.screens.library

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.screens.library.SelectedLibraryItem.NONE
import com.github.goldy1992.mp3player.commons.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val LOG_TAG = "LibraryChips"

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
    val items = mutableListOf<ChipAnimationState>()
    val stateMap = mutableMapOf<String, ChipAnimationState>()
    val closeIconVisible = currentItem != NONE
    val iconPlacementPosition = remember { Animatable(0f) }
    val iconOpacity by animateFloatAsState(targetValue = if (closeIconVisible) 1f else 0f, label = "CloseIconOpacity")
    val iconChip = ChipAnimationState("close", iconPlacementPosition, closeIconVisible, iconOpacity )
    items.add(iconChip)
    stateMap["close"] = iconChip
    for (e in SelectedLibraryItem.values()) {
        if (e != NONE) {
            val visible = (currentItem == e || currentItem == NONE)
            val placement = remember { Animatable(0f) }
            val opacity by animateFloatAsState(
                targetValue = if (visible) 1f else 0f,
                label = "${e.name}Opacity"
            )
            val chipState = ChipAnimationState(e.name, placement, visible, opacity)
            items.add(chipState)
            stateMap[e.name] = chipState
        }
    }

    CustomChipLayout(modifier = Modifier,
        chips = items) {

        IconButton(modifier = Modifier.alpha(iconOpacity), onClick = { onSelected(NONE) }) {
            Icon(Icons.Filled.Close, contentDescription = "remove filter")
        }

        for (e in SelectedLibraryItem.values()) {
            if (e != NONE) {
                FilterChip(
                    modifier = Modifier.alpha(stateMap[e.name]?.opacity ?: 0f),
                    onClick = { onSelected(e) },
                    label = {
                        Text(e.name)
                    },
                    selected = currentItem == e,
                )
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

@Preview
@Composable
private fun TestLibraryChips() {
    var chipState by remember { mutableStateOf(NONE) }

    LibraryChips(
        currentItem = chipState,
        onSelected = { v -> chipState = v }
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
@Composable
private fun CustomChipLayout(
    modifier: Modifier = Modifier,
    chips: List<ChipAnimationState>,
    scope : CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    val defaultConstraints = Constraints()
    Layout(modifier = modifier,
        content = content) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(defaultConstraints)
        }

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Track the y co-ord we have placed children up to
            var xPosition = 0

            // Place children in the parent layout
            placeables.forEachIndexed { index, placeable ->
                val currentChip = chips[index]
                if (index == 0) {
                    if (currentChip.opacity > 0 ) {
                        Log.i(LOG_TAG, "${currentChip.name} at point 0")
                        val placePosX = xPosition
                        scope.launch {currentChip.placementPosition.animateTo(placePosX.toFloat()) }
                        placeable.placeRelative(currentChip.placementPosition.value.toInt(), 0)
                        if (currentChip.visible) {
                            xPosition += placeable.width + 10
                            Log.i(LOG_TAG, "new xPos: $xPosition")
                        }
                    }
                } else {
                    if (currentChip.visible) {
                        if(currentChip.name == SelectedLibraryItem.SONGS.name) {
                            Log.i(
                                LOG_TAG,
                                "chip: ${currentChip.name} is visible, chipPlacement: (float=${currentChip.placementPosition.value}) int(${currentChip.placementPosition.value.toInt()}), desiredValue: (int=${xPosition}) (float=${xPosition.toFloat()})"
                            )
                        }
                        val placeableXPos = xPosition
                        scope.launch {
                            if (currentChip.name == SelectedLibraryItem.SONGS.name) {
                                Log.i(
                                    LOG_TAG,
                                    "calling on coroutine scope, setting songs xPos to $placeableXPos"
                                )
                            }
                            currentChip.placementPosition.animateTo(placeableXPos.toFloat())
                        }
                        placeable.placeRelative(currentChip.placementPosition.value.toInt(), 0)
                        xPosition += placeable.width + 10
                        Log.i(LOG_TAG, "new xPos: $xPosition")

                    } else if (currentChip.opacity > 0) {
                        placeable.placeRelative(currentChip.placementPosition.value.toInt(), 0)
                    }
                }

            }
        }


    }
}


private data class ChipAnimationState(
    val name: String,
    val placementPosition: Animatable<Float, AnimationVector1D>,
    val visible : Boolean,
    val opacity : Float
)

private data class LayoutState(
    val visibleChips: List<ChipAnimationState>,
    val hiddenChips: List<ChipAnimationState>
)