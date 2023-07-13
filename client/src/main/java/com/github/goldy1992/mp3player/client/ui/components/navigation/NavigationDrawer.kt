@file:OptIn(ExperimentalAnimationApi::class)

package com.github.goldy1992.mp3player.client.ui.components.navigation

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.AboutDialog
import com.github.goldy1992.mp3player.client.ui.components.RatingDialog
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@Preview
@Composable
fun NavigationDrawerContent(navController: NavController = rememberAnimatedNavController(),
                            currentScreen : Screen = Screen.LIBRARY,
                            context : Context = LocalContext.current) {

    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))

        Text(
            text = context.getString(R.string.app_title),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 28.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))

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
            selected = currentScreen == Screen.LIBRARY,
            onClick = {
                if (currentScreen != Screen.LIBRARY) {
                    navController.navigate(Screen.LIBRARY.name)
                }
            }
        )

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
            selected = currentScreen == Screen.SEARCH,
            onClick = {
                if (currentScreen != Screen.SEARCH) {
                    navController.navigate(Screen.SEARCH.name)
                }
            })

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
            selected = currentScreen == Screen.VISUALIZER,
            badge = {
                Text(text = "Beta", style = MaterialTheme.typography.labelLarge)
            },
            onClick = {
                if (currentScreen != Screen.VISUALIZER)
                    navController.navigate(Screen.VISUALIZER.name)
            })

        Divider(
            modifier = Modifier.padding(
                top = 16.dp,
                end = 28.dp
            ),
            //   startIndent = 28.dp,
            color = MaterialTheme.colorScheme.outline
        )

        var openRatingDialog by remember { mutableStateOf(false) }
        if (openRatingDialog) {
            RatingDialog {
                openRatingDialog = false
            }
        }
        NavigationDrawerItem(modifier = Modifier
            .padding(horizontal = 12.dp),
            label = {
                Text(
                    text = "Review",
                    style = MaterialTheme.typography.labelLarge
                )
            },
            icon = {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Equalizer",
                    //    modifier = Modifier.size(24.dp)
                )
            },
            selected = false,

            onClick = {
                if (!openRatingDialog) {
                    openRatingDialog = true
                }
            }
        )

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
            selected = currentScreen == Screen.SETTINGS,
            onClick = {
                if (currentScreen != Screen.SETTINGS) {
                    navController.navigate(Screen.SETTINGS.name)
                }
            })
    }
}


