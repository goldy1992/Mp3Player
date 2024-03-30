package com.github.goldy1992.mp3player.client.ui.screens.library

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier,
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

    // extraItems for scroll
    for (i in 1..5) {
        val iconPlacementPosition = remember { Animatable(0f) }
        val iconOpacity = 1f
        val iconChip = ChipAnimationState("home", iconPlacementPosition, true, iconOpacity )
        items.add(iconChip)
    }

    CustomChipLayout(modifier = modifier,
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

        for (i in 1..5) {
            Icon(Icons.Filled.Home, contentDescription = "")
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
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color.Green)) {
        LibraryChips(
            currentItem = chipState,
            onSelected = { v -> chipState = v }
        )

    }
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
        var measuredHeight = 0
        val placeables = measurables.map { measurable ->
            // Measure each children
            val placeable = measurable.measure(defaultConstraints)
            if (placeable.height > measuredHeight) {
                measuredHeight = placeable.height
            }
            placeable
        }

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, measuredHeight) {
            // Track the x co-ord we have placed children up to
            var xPosition = 0

            // Place children in the parent layout
            placeables.forEachIndexed { index, placeable ->
                val currentChip = chips[index]
                val shouldPlace = currentChip.visible || currentChip.opacity > 0
                if (shouldPlace) {
                    if (currentChip.visible) {
                        val placePosX = xPosition
                        scope.launch {currentChip.placementPosition.animateTo(placePosX.toFloat()) }
                        xPosition += placeable.width + 10
                    }
                    placeable.placeRelative(currentChip.placementPosition.value.toInt(), 0)
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