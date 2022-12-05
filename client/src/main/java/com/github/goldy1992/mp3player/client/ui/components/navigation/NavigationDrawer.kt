@file:OptIn(ExperimentalAnimationApi::class)

package com.github.goldy1992.mp3player.client.ui.components.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@ExperimentalMaterialApi
@Composable
fun NavigationDrawer(navController: NavController,
                    modifier : Modifier = Modifier) {
    Column(modifier = modifier
        .background(MaterialTheme.colorScheme.surface)
        .fillMaxWidth()) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.headphone_icon),
                contentDescription = "Menu icon"
            )
            Card() {
                Text(modifier = Modifier.padding(start = 10.dp), text = "Menu", style = MaterialTheme.typography.labelLarge, )
            }

            Divider()
            LibraryItem(navController = navController)
            SettingsItem(navController = navController)
        }
    }

}


@Preview
@ExperimentalMaterialApi
@Composable
fun LibraryItem(navController: NavController = rememberAnimatedNavController(),
                selected : Boolean = true) {
    val library = stringResource(id = R.string.library)
    ListItem(
        icon = { Icon(Icons.Filled.LibraryMusic, contentDescription = library) },
        text = { Text(library) },
        modifier = Modifier
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.background
            )
            .clickable { navController.navigate(Screen.MAIN.name) }
    )
}


@ExperimentalMaterialApi
@Composable
fun SettingsItem(navController: NavController) {
    val settings = stringResource(id = R.string.settings)
    ListItem(
        icon = { Icon(Icons.Filled.Settings, contentDescription = settings) },
        text = { Text(settings) },
        modifier = Modifier.clickable { navController.navigate(Screen.SETTINGS.name) }
    )

}

@ExperimentalMaterialApi
@Composable
fun VisualizerItem(navController: NavController) {
    ListItem(
        icon = { Icon(Icons.Filled.Equalizer, contentDescription = "Equalizer") },
        text = { Text("Equalizer") },
        modifier = Modifier.clickable { navController.navigate(Screen.VISUALIZER.name) }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NavigationDrawerContent(navController: NavController = rememberAnimatedNavController(),
                            currentScreen : Screen = Screen.LIBRARY) {

    ModalDrawerSheet() {


        Image(
            modifier = Modifier
                .padding(vertical = 20.dp, horizontal = 12.dp)
                .size(40.dp),
            painter = painterResource(id = R.drawable.headphone_icon),
            contentDescription = "Menu icon"
        )
        val library = stringResource(id = R.string.library)
        NavigationDrawerItem(
            modifier = Modifier.padding(horizontal = 12.dp),
            label = {
                Text(
                    text = library,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
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
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
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

        val settings = stringResource(id = R.string.settings)
        NavigationDrawerItem(
            modifier = Modifier.padding(horizontal = 12.dp),
            label = {
                Text(
                    text = settings,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
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
        NavigationDrawerItem(
            modifier = Modifier.padding(horizontal = 12.dp),
            label = {
                Text(
                    text = "Equalizer",
                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                )
            },
            icon = { Icon(Icons.Filled.Equalizer,
                contentDescription = "Equalizer",
                modifier = Modifier.size(24.dp)) },
            selected = currentScreen == Screen.VISUALIZER,
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
    }
}

