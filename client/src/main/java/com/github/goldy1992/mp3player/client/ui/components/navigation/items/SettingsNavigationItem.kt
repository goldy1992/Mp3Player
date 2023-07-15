package com.github.goldy1992.mp3player.client.ui.components.navigation.items

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R

@Preview
@Composable
fun SettingsNavigationItem(
    isSelected : Boolean = false,
    onClick : () -> Unit = {}
) {
    val settings = stringResource(id = R.string.settings)
    NavigationDrawerItem(
        modifier = Modifier.padding(horizontal = 12.dp),
        label = {
            Text(
                text = settings,
                style = MaterialTheme.typography.labelLarge
            )
        },
        icon = {
            Icon(
                Icons.Filled.Settings,
                contentDescription = settings,
                modifier = Modifier.size(24.dp)
            )
        },
        selected = isSelected,
        onClick = onClick
    )


}