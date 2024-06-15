package com.github.goldy1992.mp3player.client.ui.screens.library

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
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
    showNavIcon: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),

    onClickSearchIcon : () -> Unit,
    onClickNavIcon : () -> Unit
) {
    val navigationDrawerIconDescription = stringResource(id = R.string.navigation_drawer_menu_icon)

    val titleComposable : @Composable () -> Unit = {
        Text(text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }

    val actions : @Composable RowScope.() -> Unit = {
        IconButton(onClick = onClickSearchIcon) {
            Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search")
        }
    }

    if (showNavIcon) {

        val navIcon : @Composable () -> Unit = {
            IconButton(
                onClick = onClickNavIcon,
                modifier = Modifier.semantics {
                    contentDescription = navigationDrawerIconDescription
                })
            {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Menu Btn")
            }
        }
        TopAppBar(
            title = titleComposable,
            actions = actions,
            scrollBehavior = scrollBehavior,
            navigationIcon = navIcon
        )
    } else {
        TopAppBar(
            title = titleComposable,
            actions = actions,
            scrollBehavior = scrollBehavior,
        )
    }

}


