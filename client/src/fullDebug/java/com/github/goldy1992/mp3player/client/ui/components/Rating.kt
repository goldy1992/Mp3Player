package com.github.goldy1992.mp3player.client.ui.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Rating() {
    var currentRating by remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Row(horizontalArrangement = Arrangement.Center) {
            for (i in 1..5) {
                RatingStar(isSelected  = currentRating >= i) { currentRating = i }
            }
        }
        Text("")
    }
}

@Preview
@Composable
fun RatingStar(
    modifier: Modifier = Modifier,
    isSelected : Boolean= false,
    onClick: () -> Unit = {},
) {
         val interactionSource = remember { MutableInteractionSource() }
        val pressed by interactionSource.collectIsPressedAsState()
        val color: Color by animateColorAsState(if (isSelected) Color.Yellow else Color.Gray)
        val size by animateDpAsState(
            targetValue = if (pressed) 100.dp else 48.dp,
            animationSpec = if (pressed) tween(100) else spring(Spring.DampingRatioHighBouncy, stiffness = 500f)

        )

        IconButton(
            onClick = {
                onClick()
            },
            interactionSource = interactionSource,
            modifier = modifier.size(75.dp)
        ) {
            Icon(
                tint = color,
                imageVector = Icons.Outlined.Star,
                contentDescription = "star 1",
                modifier = Modifier
//                .animateContentSize { initialValue, targetValue ->  }
                    .size(size)
            )

        }


}

