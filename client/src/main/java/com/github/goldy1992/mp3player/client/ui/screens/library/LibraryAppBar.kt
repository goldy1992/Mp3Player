package com.github.goldy1992.mp3player.client.ui.screens.library

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LibraryAppBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    windowSize: WindowSize
) {

    val title : @Composable () -> Unit = {
        Text(text = "Library")
    }

    val actions : @Composable RowScope.() -> Unit = {
        IconButton(onClick = { navController.navigate(Screen.SEARCH.name) }) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        }
    }

    val isLargeScreen = windowSize == WindowSize.Expanded

    if (isLargeScreen) {
        LargeAppBar(title = title,
        actions = actions)
    } else {
        SmallAppBar(
            scaffoldState = scaffoldState,
            scope = scope,
            title = title,
            actions = actions)
    }
}

@Composable
private fun LargeAppBar(
    title : @Composable () -> Unit,
    actions : @Composable RowScope.() -> Unit
) {
    TopAppBar(
        title = title,
        actions = actions)

}

@Composable
private fun SmallAppBar(
    scope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    title : @Composable () -> Unit,
    actions : @Composable RowScope.() -> Unit
) {

    val navigationDrawerIconDescription = stringResource(id = R.string.navigation_drawer_menu_icon)

    TopAppBar(
        title = title,
        actions = actions,
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        if (scaffoldState.drawerState.isClosed) {
                            scaffoldState.drawerState.open()
                        }
                    }
                },
                modifier = Modifier.semantics {
                    contentDescription = navigationDrawerIconDescription
                })
            {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Btn")
            }
        },

    )
}
