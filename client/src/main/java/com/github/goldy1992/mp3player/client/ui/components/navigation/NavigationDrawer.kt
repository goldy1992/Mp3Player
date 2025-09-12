package com.github.goldy1992.mp3player.client.ui.components.navigation

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.LibraryNavigationDrawerItem
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.ReviewNavigationItem
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.SearchNavigationItem
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.SettingsNavigationItem
import com.github.goldy1992.mp3player.client.ui.components.navigation.items.VisualizerNavigationIcon
import com.github.goldy1992.mp3player.client.Screen


@Preview
@Composable
fun NavigationDrawerContent(navController: NavController = rememberNavController(),
                            currentScreen : Screen = Screen.LIBRARY,
                            context : Context = LocalContext.current) {

    ModalDrawerSheet {
        LazyColumn {
            item {
                Spacer(Modifier.height(12.dp))
            }
            item {
                Text(
                    text = context.getString(R.string.app_title),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 28.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                Spacer(Modifier.height(24.dp))
            }

            item {
                val isLibraryScreenSelected =
                    Screen.LIBRARY.name == navController.currentDestination?.route
                LibraryNavigationDrawerItem(isSelected = isLibraryScreenSelected) {
                    NavigationActions.navigateToLibrary(navController)
                }
            }

            item {
                val isSearchScreenSelected = currentScreen == Screen.SEARCH
                SearchNavigationItem(isSelected = isSearchScreenSelected) {
                    if (!isSearchScreenSelected) {
                        navController.navigate(Screen.SEARCH.name)
                    }
                }
            }
            item {
                val isEqualizerScreenSelected = currentScreen == Screen.VISUALIZER_COLLECTION
                VisualizerNavigationIcon(isSelected = isEqualizerScreenSelected) {
                    if (!isEqualizerScreenSelected) {
                        navController.navigate(Screen.VISUALIZER_COLLECTION.name)
                    }
                }
            }

            item {
                HorizontalDivider(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        end = 28.dp
                    ),
                    color = MaterialTheme.colorScheme.outline
                )
            }

            item {
                ReviewNavigationItem()
            }

            item {
                val isSettingsScreenSelected = currentScreen == Screen.SETTINGS
                SettingsNavigationItem(isSelected = isSettingsScreenSelected) {
                    if (!isSettingsScreenSelected) {
                        navController.navigate(Screen.SETTINGS.name)
                    }
                }
            }

        }
    }
}



