package com.github.goldy1992.mp3player.client.ui.screens.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.github.goldy1992.mp3player.client.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallAppBar(
    title : String = "title",
    onClickSearchIcon : () -> Unit,
    onClickNavIcon : () -> Unit
) {
    val navigationDrawerIconDescription = stringResource(id = R.string.navigation_drawer_menu_icon)

    TopAppBar(
        title = {
            Text(text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        actions = {
            IconButton(onClick = onClickSearchIcon) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        }
},
        navigationIcon = {
            IconButton(
                onClick = onClickNavIcon,
                modifier = Modifier.semantics {
                    contentDescription = navigationDrawerIconDescription
                })
            {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Btn")
            }
        },

    )

}


