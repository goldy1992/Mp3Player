@file:OptIn(ExperimentalAnimationApi::class)

package com.github.goldy1992.mp3player.client.ui.components.navigation

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.EqualizerNavigationIcon
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.LibraryNavigationItem
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.ReviewNavigationItem
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.SearchNavigationItem
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.SettingsNavigationItem
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

        val isLibraryScreenSelected = currentScreen == Screen.LIBRARY
        LibraryNavigationItem(isSelected = isLibraryScreenSelected) {
            if (!isLibraryScreenSelected) {
                navController.navigate(Screen.LIBRARY.name)
            }
        }

        val isSearchScreenSelected = currentScreen == Screen.SEARCH
        SearchNavigationItem(isSelected = isSearchScreenSelected) {
            if (!isSearchScreenSelected) {
                navController.navigate(Screen.SEARCH.name)
            }
        }

        val isEqualizerScreenSelected = currentScreen == Screen.VISUALIZER
        EqualizerNavigationIcon(isSelected = isEqualizerScreenSelected) {
            if (!isEqualizerScreenSelected) {
                navController.navigate(Screen.VISUALIZER.name)
            }
        }

        Divider(
            modifier = Modifier.padding(
                top = 16.dp,
                end = 28.dp
            ),
            color = MaterialTheme.colorScheme.outline
        )

        ReviewNavigationItem()

        val isSettingsScreenSelected = currentScreen == Screen.SETTINGS
        SettingsNavigationItem(isSelected = isSettingsScreenSelected) {
            if (!isSettingsScreenSelected) {
                navController.navigate(Screen.SETTINGS.name)
            }
        }

    }
}


