package com.github.goldy1992.mp3player.client.ui.components.navigation.items

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun EqualizerNavigationIcon(
    isSelected : Boolean = false,
    onClick : () -> Unit = {}
) {

    NavigationDrawerItem(
        modifier = Modifier
            .padding(horizontal = 12.dp),
        label = {
            Text(
                text = "Equalizer",
                style = MaterialTheme.typography.labelLarge
            )
        },
        icon = {
            Icon(
                Icons.Filled.Equalizer,
                contentDescription = "Equalizer",
                modifier = Modifier.size(24.dp)
            )
        },
        selected = isSelected,
        badge = {
            Text(text = "Beta", style = MaterialTheme.typography.labelLarge)
        },
        onClick = onClick)
}
