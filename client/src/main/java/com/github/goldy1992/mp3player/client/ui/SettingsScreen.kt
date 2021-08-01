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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 *
 */
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
            ThemeSubheader()
            ThemeMenuItem(navController)
            SystemDarkModeMenuItem(
                userPreferencesRepository = userPreferencesRepository,
                scope = scope,
                useSystemDarkMode = useSystemDarkMode
            )
            DarkModeMenuItem(
                userPreferencesRepository = userPreferencesRepository,
                scope = scope,
                useSystemDarkMode = useSystemDarkMode,
                isDarkMode = isDarkMode
            )
            Divider()
            HelpSubHeader()
            SupportAndFeedbackMenuItem(navController)
            VersionMenuItem()
        }
    })
}

@ExperimentalMaterialApi
@Composable
private fun ThemeSubheader() {
    ListItem(
        text = { Text(stringResource(id = R.string.display), style = MaterialTheme.typography.subtitle2) },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@ExperimentalMaterialApi
@Composable
private fun ThemeMenuItem(navController: NavController) {
    val theme = stringResource(id = R.string.theme)
    ListItem(
        icon = { Icon(Icons.Filled.Palette, contentDescription = theme) },
        text = { Text(theme, style = MaterialTheme.typography.subtitle1) },
        modifier = Modifier.clickable { navController.navigate(Screen.THEME_SELECT.name)}
    )
}

@ExperimentalMaterialApi
@Composable
private fun SystemDarkModeMenuItem(userPreferencesRepository: UserPreferencesRepository,
                                    scope: CoroutineScope,
                                    useSystemDarkMode : Boolean) {

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
}

@ExperimentalMaterialApi
@Composable
private fun DarkModeMenuItem(userPreferencesRepository: UserPreferencesRepository,
                            scope: CoroutineScope,
                            isDarkMode : Boolean,
                            useSystemDarkMode: Boolean){
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

}

@ExperimentalMaterialApi
@Preview
@Composable
private fun HelpSubHeader() {
    ListItem(
        text = { Text(stringResource(id = R.string.help), style = MaterialTheme.typography.subtitle2) },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@ExperimentalMaterialApi
@Composable
private fun SupportAndFeedbackMenuItem(navController: NavController) {
    val supportAndFeedback = stringResource(id = R.string.support_and_feedback)
    ListItem(
        icon = { Icon(Icons.Filled.Help, contentDescription = supportAndFeedback) },
        text = {
            Column() {
                Text(supportAndFeedback)
            }
        },
    )
}

@ExperimentalMaterialApi
@Composable
private fun VersionMenuItem() {
    val context = LocalContext.current
    val pInfo: PackageInfo = context.packageManager
        .getPackageInfo(context.packageName, 0)
    val version = pInfo.versionName
    ListItem(
        icon={},
        text = {
            Column() {
                Text(stringResource(id = R.string.version))

                Text(version, style= MaterialTheme.typography.caption)
            }
        },
    )
}

@ExperimentalMaterialApi
@Composable
private fun AboutMenuItem(navController: NavController) {
    ListItem(
        icon={ },
        text = {
            Column() {
                Text("About") // TODO: Translate and link to about page!
            }
        },
    )
}