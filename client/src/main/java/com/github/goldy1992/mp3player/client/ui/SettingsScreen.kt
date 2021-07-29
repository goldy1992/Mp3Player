package com.github.goldy1992.mp3player.client.ui

import android.content.pm.PackageInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
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
        TopAppBar(title = { Text(text = stringResource(id = R.string.settings))},
        navigationIcon =  {
            NavUpButton(
                navController = navController,
                scope = scope) },
        backgroundColor = MaterialTheme.colors.primary)
    },
    content = {
        Column(modifier = Modifier.padding(DEFAULT_PADDING)) {
            ListItem(
                text = { Text(stringResource(id = R.string.display), style = MaterialTheme.typography.subtitle2) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(THEME_SELECT_SCREEN) }
            )
            ListItem(
                icon = { Icon(Icons.Filled.Palette, contentDescription = "Theme") },
                text = { Text(stringResource(id = R.string.theme), style = MaterialTheme.typography.subtitle1) },
                modifier = Modifier.clickable { navController.navigate(THEME_SELECT_SCREEN)}
            )
            ListItem(modifier = Modifier.fillMaxWidth(),
                icon = { Icon(Icons.Default.DarkMode, contentDescription = "Use System Dark Mode") },
                text = { Text(text = stringResource(id = R.string.use_system_dark_mode), style = MaterialTheme.typography.subtitle1)},
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
                text = { Text(text = stringResource(id = R.string.dark_mode)) },
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

            Divider()
            ListItem(
                text = { Text(stringResource(id = R.string.help), style = MaterialTheme.typography.subtitle2) },
                modifier = Modifier
                    .fillMaxWidth()
            )

            ListItem(
                icon = { Icon(Icons.Filled.Help, contentDescription = "Theme") },
                text = {
                    Column() {
                        Text(stringResource(id = R.string.support_and_feedback))
                    }
                },
            )

            ListItem(
                icon={},
                text = {
                    Column() {
                        Text(stringResource(id = R.string.version))
                        val context = LocalContext.current
                        val pInfo: PackageInfo = context.packageManager
                            .getPackageInfo(context.packageName, 0)
                        val version = pInfo.versionName

                        Text(version, style= MaterialTheme.typography.caption)
                    }
                },
            )
        }
    })
}
