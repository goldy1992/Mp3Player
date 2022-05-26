package com.github.goldy1992.mp3player.client.ui.screens.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SmallLibraryAppBar(
    scope: CoroutineScope,
    navController: NavController,
    onClickNavIcon : suspend () -> Unit
) {
    val navigationDrawerIconDescription = stringResource(id = R.string.navigation_drawer_menu_icon)

    SmallTopAppBar(
        title = {
            Text(text = "Library",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.SEARCH.name) }) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        }
},
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        onClickNavIcon()
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


