package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import kotlinx.coroutines.launch
import java.io.File

@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
    userPreferencesRepository: UserPreferencesRepository,
    navController : NavController) {

    val scope = rememberCoroutineScope()
    val useSystemDarkMode by userPreferencesRepository.getSystemDarkMode().collectAsState(initial = false)
    val isDarkMode by userPreferencesRepository.getDarkMode().collectAsState(initial = false)

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Settings")},
        navigationIcon =  {
            NavUpButton(
                navController = navController,
                scope = scope) },
        backgroundColor = MaterialTheme.colors.primary)
    },
    content = {
        Column(modifier = Modifier.padding(DEFAULT_PADDING)) {
            ListItem(
                text = { Text("Display", style = MaterialTheme.typography.subtitle2) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(THEME_SELECT_SCREEN) }
            )
            ListItem(
                icon = { Icon(Icons.Filled.Palette, contentDescription = "Theme") },
                text = { Text("Theme", style = MaterialTheme.typography.subtitle1) },
                modifier = Modifier.clickable { navController.navigate(THEME_SELECT_SCREEN)}
            )
            ListItem(modifier = Modifier.fillMaxWidth(),
                icon = { Icon(Icons.Default.DarkMode, contentDescription = "Use System Dark Mode") },
                text = { Text(text = "Use System Dark Mode", style = MaterialTheme.typography.subtitle1)},
                trailing = {
                    Switch(
                        checked = useSystemDarkMode,
                        onCheckedChange = { isChecked ->
                            scope.launch {
                                userPreferencesRepository.updateSystemDarkMode(isChecked)
                            }
                        },
                        colors = SwitchDefaults.colors()
                    )
                }
            )
            ListItem(modifier = Modifier.fillMaxWidth(),
                icon = { Icon(Icons.Default.DarkMode, contentDescription = "Dark Mode") },
                text = { Text(text = "Dark Mode") },
                trailing = {
                    Switch(
                    checked = isDarkMode,
                    enabled = !useSystemDarkMode,
                    onCheckedChange = { isChecked ->
                        scope.launch {
                            userPreferencesRepository.updateDarkMode(isChecked)
                        }
                    })
                })
        }
    })
}