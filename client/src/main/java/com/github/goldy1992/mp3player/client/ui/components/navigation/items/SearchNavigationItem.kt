package com.github.goldy1992.mp3player.client.ui.components.navigation.items

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R

@Preview
@Composable
fun SearchNavigationItem(
    isSelected : Boolean = false,
    onClick : () -> Unit = {}
) {
    val search = stringResource(id = R.string.search)
    NavigationDrawerItem(
        modifier = Modifier.padding(horizontal = 12.dp),
        label = {
            Text(
                text = search,
                style = MaterialTheme.typography.labelLarge
            )
        },
        icon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = search,
                modifier = Modifier.size(24.dp)
            )
        },
        selected = isSelected,
        onClick = onClick)

}

@Preview
@Composable
fun SearchNavigationRailItem(
    isSelected : Boolean = false,
    onClick : () -> Unit = {}
) {
    val search = stringResource(id = R.string.search)
    NavigationRailItem(
        modifier = Modifier.padding(horizontal = 12.dp),
        icon = { Icon(Icons.Filled.Search, contentDescription = search) },
        selected = isSelected,
        onClick = onClick
    )
}