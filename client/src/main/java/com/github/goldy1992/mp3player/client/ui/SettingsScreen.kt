package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import kotlinx.coroutines.launch

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
        Column {
            Text(text = "Display",
                style = MaterialTheme.typography.caption)
            ListItem(icon = { Icon(Icons.Filled.Palette, contentDescription = "Theme") },
                text = { Text("Theme") },
                modifier = Modifier.clickable { navController.navigate(THEME_SELECT_SCREEN) }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.DarkMode, contentDescription = "Use System Dark Mode")
                Text(text = "Use System Dark Mode")
                Switch(
                    checked = useSystemDarkMode,
                    onCheckedChange = { isChecked ->
                        scope.launch {
                            userPreferencesRepository.updateSystemDarkMode(isChecked)
                        }
                    })
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.DarkMode, contentDescription = "Dark Mode")
                Text(text = "Dark Mode")
                Switch(
                    checked = isDarkMode,
                    enabled = !useSystemDarkMode,
                    onCheckedChange = { isChecked ->
                        scope.launch {
                            userPreferencesRepository.updateDarkMode(isChecked)
                        }
                    }
                )
            }
        }
    })
}

