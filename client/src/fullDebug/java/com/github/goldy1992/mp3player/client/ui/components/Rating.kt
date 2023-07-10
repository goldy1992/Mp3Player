package com.github.goldy1992.mp3player.client.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val rating_text = listOf(
    "We need to talk",
    "Passable",
    "Not bad",
    "I like it",
    "I love it!"
)

data class PressedState(
    val isPressed: Boolean = false,
    val numberPressed : Int = 1
) {

}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Rating() {
    var currentRating by remember { mutableStateOf(1) }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Row(horizontalArrangement = Arrangement.Center) {

            var pressedState by remember { mutableStateOf(PressedState(false, 0))
            }
            for (i in 1..5) {
                val interactionSource = remember { MutableInteractionSource() }
                val pressed by interactionSource.collectIsPressedAsState()

                LaunchedEffect(pressed) {
                    pressedState = PressedState(pressed, i)
                }

                RatingStar(isSelected  = currentRating >= i, size = 50.dp, isPressed = (pressedState.isPressed && pressedState.numberPressed >= i),  interactionSource = interactionSource) { currentRating = i }
            }
        }
        Spacer(Modifier.height(20.dp))
        if (currentRating > 0) {
            Text(text = rating_text[currentRating - 1], modifier = Modifier.animateContentSize(), style = MaterialTheme.typography.bodyMedium, color  = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview
@Composable
private fun RatingStar(
    modifier: Modifier = Modifier,
    size : Dp = 100.dp,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    isSelected : Boolean= false,
    isPressed: Boolean = false,
    onClick: () -> Unit = {},
) {
        val originalSize by remember(size) {
            mutableStateOf( Dp(size.value * 0.6f))

        }
        val color: Color by animateColorAsState(if (isSelected) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.surfaceVariant)
        val animatedSize by animateDpAsState(
            targetValue = if (isPressed) size else originalSize,
            animationSpec = if (isPressed) tween(100) else spring(Spring.DampingRatioHighBouncy, stiffness = 500f)

        )

        IconButton(
            onClick = {
                onClick()
            },
            interactionSource = interactionSource,
            modifier = modifier
                .size(size)

        ) {
            Icon(
                tint = color,
                imageVector = Icons.Outlined.Star,
                contentDescription = "star 1",
                modifier = Modifier
                    .size(animatedSize)
            )

        }


}

