package com.github.goldy1992.mp3player.client.ui.components.navigation.items

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
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
fun LibraryNavigationItem(
    isSelected : Boolean = false,
    onClick : () -> Unit = {}
) {
    val library = stringResource(id = R.string.library)
    NavigationDrawerItem(
        modifier = Modifier.padding(horizontal = 12.dp),
        label = {
            Text(
                text = library,
                style = MaterialTheme.typography.labelLarge
            )
        },
        icon = { Icon(Icons.Filled.LibraryMusic, contentDescription = library) },
        selected = isSelected,
        onClick = onClick
    )
}